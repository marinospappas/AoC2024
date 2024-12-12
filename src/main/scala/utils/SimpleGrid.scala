package org.mpdev.scala.aoc2024
package utils

import utils.SimpleGrid.Direction
import utils.SimpleGrid.Direction.*

import java.util
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

open class SimpleGrid(gridData: List[String]) {

    private val data: Array[Array[Char]] = gridData.toArray.map (s => s.toCharArray)
    private val maxX: Int = data(0).length - 1
    private val maxY: Int = data.length - 1

    def getDataPoint(p: (Int, Int)): Char = data(p._2)(p._1)

    def getDataPointOrNull(p: (Int, Int)): Char | Null =
        if isInsideGrid(p) then data(p._2)(p._1) else null
    
    def setDataPoint(p: (Int, Int), d: Char): Unit = data(p._2)(p._1) = d

    def getDataPoints: Array[Array[Char]] = data

    def getDataValues: Set[Char] = data.flatten.toSet

    def getAllCoordinates: Vector[(Int, Int)] =
        (for x <- 0 to maxX; y <- 0 to maxY yield (x, y)).toVector

    // TODO: tidy this up to take (Int, Int) as input
    def getAdjacent(p: Point, includeDiagonals: Boolean = false): Set[Point] =
        (if (includeDiagonals) p.adjacent() else p.adjacentCardinal()).toSet

    def getAdjacentValues(p: (Int, Int), includeDiagonals: Boolean = false): List[Char] = {
        val point = Point(p._1, p._2)
        (if (includeDiagonals) point.adjacent() else point.adjacentCardinal())
            .map( pos => data(pos.y)(pos.x) ).toList
    }

    def findFirst(d: Char): (Int, Int) = {
        val y = (0 to maxY).find( data(_).contains(d) ).getOrElse(-1)
        if y >= 0 then (data(y).indexOf(d), y) else (-1, -1)
    }

    def findAll(d: Char): Set[(Int, Int)] =
        (for {
            y <- 0 to maxY
            x <- 0 to maxX
            if data(y)(x) == d
        } yield (x, y)).toSet

    def getColumn(x: Int): List[Char] = (0 to maxY).map( data(x)(_)).toList

    def getRow(y: Int): List[Char] = data(y).toList

    def getDimensions: (Int, Int) = (maxX+1, maxY+1)

    def countOf(item: Char): Int = data.map(_.toList).toList.flatten.count( _ == item )
    
    def nextPoint(p: (Int, Int)): (Int, Int) = 
        if (p._1 < maxX) (p._1 + 1, p._2)
        else (0, p._2 + 1)

    def isInsideGrid(p: (Int, Int)): Boolean = 0 <= p._1 && p._1 <= maxX && 0 <= p._2 && p._2 <= maxY

    // TODO: need also indexToPos
    def posToIndex(pos: (Int, Int)): Int = pos._2 * maxY + pos._1

    def getAdjacentArea(point: (Int, Int)): Vector[(Int, Int)] = {
        val visited = ArrayBuffer(point)
        val queue = util.ArrayDeque[(Int, Int)]()
        queue.add(point)
        val value = data(point._2)(point._1)
        while !queue.isEmpty do {
            val current = queue.removeFirst()
            current.adjacentCardinal.filter( p => isInsideGrid(p) && data(p._2)(p._1) == value )
                .foreach ( connection =>
                    if !visited.contains(connection) then {
                        visited += connection
                        queue.add(connection)
                    }
            )
        }
        visited.toVector
    }

    def getPerimeter(area: Vector[(Int, Int)]): Int = {
        var perimeter = 0
        area.foreach( point =>
            val adjacentInArea = point.adjacentCardinal.filter( p => area.contains(p) )
            perimeter += (4 - adjacentInArea.size)
        )
        perimeter
    }

    def getNumberOfSides(area: Vector[(Int, Int)]): Int = {
        val horizGroups = area.groupBy(_._2).map( (k, v) => (k, v.sortBy( _._1 )) ).toVector.sortBy(_._1)
        val vertGroups = area.groupBy(_._1).map( (k, v) => (k, v.sortBy( _._2 )) ).toVector.sortBy(_._1)
        countVertEdges(horizGroups) + countHorizEdges(vertGroups)
    }

    private def countVertEdges(ptsGrouped: Vector[(Int, Vector[(Int, Int)])]): Int = {
        var numberOfEdges = 0
        var prevEdges = Set[(Int, Direction)]()
        ptsGrouped.foreach(g =>
            val thisEdges = mutable.Set((g._2.head._1, W))
            for i <- 1 until g._2.size do 
                if g._2(i)._1 != g._2(i - 1)._1 + 1 then {
                    thisEdges += ((g._2(i - 1)._1 + 1, E))
                    thisEdges += ((g._2(i)._1, W))
                }
            thisEdges += ((g._2.last._1 + 1, E))
            numberOfEdges += (
                if prevEdges.isEmpty then thisEdges.size 
                else thisEdges.size - thisEdges.intersect(prevEdges).size
            )  
            prevEdges = Set.from(thisEdges)
        ) 
        numberOfEdges
    }

    private def countHorizEdges(ptsGrouped: Vector[(Int, Vector[(Int, Int)])]): Int = {
        var numberOfEdges = 0
        var prevEdges = Set[(Int, Direction)]()
        ptsGrouped.foreach(g =>
            val thisEdges = mutable.Set((g._2.head._2, N))
            for i <- 1 until g._2.size do
                if g._2(i)._2 != g._2(i - 1)._2 + 1 then {
                    thisEdges += ((g._2(i - 1)._2 + 1, S))
                    thisEdges += ((g._2(i)._2, N))
                }
            thisEdges += ((g._2.last._2 + 1, S))
            numberOfEdges += (
                if prevEdges.isEmpty then thisEdges.size
                else thisEdges.size - thisEdges.intersect(prevEdges).size
            )
            prevEdges = Set.from(thisEdges)
        )
        numberOfEdges
    }
    
    def printIt(): Unit = {
        for i <- data.indices do
            (f"${i % 100}%2d " + data(i).mkString).printLn()
        ("   " + data.head.indices.map( x => if x % 10 == 0 then ((x / 10) % 10).toString else " " ).mkString).printLn()
        ("   " + data.head.indices.map( x => (x % 10).toString ).mkString).printLn()
    }
}

extension (p: (Int, Int)) {
    def adjacent(diagonally: Boolean = true): Vector[(Int, Int)] =
        val (x, y) = p
        if (diagonally)
            Vector(
                (x - 1, y), (x - 1, y - 1), (x, y - 1), (x + 1, y - 1),
                (x + 1, y), (x + 1, y + 1), (x, y + 1), (x - 1, y + 1)
            )
        else
            Vector((x - 1, y), (x, y - 1), (x + 1, y), (x, y + 1))

    def adjacentCardinal: Vector[(Int, Int)] = adjacent(false)
}

object SimpleGrid {
    val DEFAULT_CHAR = '.'
    val allCharsDefMapper: Map[Char, Char] = (' ' to '~').map(c => c -> c).toMap

    enum Direction(val incr: (Int, Int), val symbol: Char = 'x') {
        // cardinal
        case N extends Direction((0, -1), '^')
        case E extends Direction((1, 0), '>')
        case S extends Direction((0, 1), 'v')
        case W extends Direction((-1, 0), '<')

        // diagonal
        case NE extends Direction((1, -1))
        case NW extends Direction((-1, -1))
        case SE extends Direction((1, 1))
        case SW extends Direction((-1, 1))

        case NONE extends Direction((0, 0))

        def turn(leftRight: Int): Direction = if (leftRight == 1) turnRight else turnLeft

        def turnRight: Direction = this match
            case N => E
            case E => S
            case S => W
            case W => N
            case _ => NONE

        def turnLeft: Direction = this match
            case N => W
            case W => S
            case S => E
            case E => N
            case _ => NONE

        def reverse: Direction = this match
            case N => S
            case W => E
            case S => N
            case E => W
            case _ => NONE
    }

    object Direction {
        def allCardinal: Set[Direction] = Set(N, E, S, W)

        def allDirections: Set[Direction] = allCardinal ++ Set(NE, SE, SW, NW)
    }

    def compareYX(p1: (Int, Int), p2: (Int, Int)): Boolean = {
        if p1._2 < p2._2 then true
        else if p1._2 > p2._2 then false
        else p1._1 < p2._1
    }
}

