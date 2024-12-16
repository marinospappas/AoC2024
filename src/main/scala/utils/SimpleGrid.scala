package org.mpdev.scala.aoc2024
package utils

import utils.SimpleGrid.Direction
import utils.SimpleGrid.Direction.*
import framework.AoCException

import java.util
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

open class SimpleGrid(gridData: Vector[String]) {

    //TODO investigate replacing Array with Vector - implication with setDataPoint
    private val data: Array[Array[Char]] = gridData.map (s => s.toCharArray).toArray
    private val maxX: Int = data(0).length - 1
    private val maxY: Int = data.length - 1

    override def clone = SimpleGrid(Vector.from(data.map( _.mkString )))

    def getDataPoint(p: (Int, Int)): Char = data(p._2)(p._1)

    def getDataPointOrNull(p: (Int, Int)): Char | Null =
        if isInsideGrid(p) then data(p.y)(p.x) else null
    
    def setDataPoint(p: (Int, Int), d: Char): Unit = data(p.y)(p.x) = d

    def getDataPoints: Vector[Vector[Char]] = data.map(_.toVector).toVector

    def getDataValues: Set[Char] = data.flatten.toSet

    def moveDataPoints(points: Vector[(Int, Int)], direction: Direction, steps: Int, replacement: Char): Unit = {
        val dataValues = points.map( p => data(p.y)(p.x) )
        val nesPoints = points.map( _ + direction.incr * steps )
        points.indices.foreach( i =>
            val newP = points(i) + direction.incr * steps
            data(newP.y)(newP.x) = dataValues(i)
            // TODO: complete this function
        )
    }

    def getAllCoordinates: Vector[(Int, Int)] =
        (for x <- 0 to maxX; y <- 0 to maxY yield (x, y)).toVector

    def getAdjacent(p: (Int, Int), includeDiagonals: Boolean = false): Set[(Int, Int)] =
        (if (includeDiagonals) p.adjacent() else p.adjacentCardinal).toSet

    def getAdjacentValues(p: (Int, Int), includeDiagonals: Boolean = false): Vector[Char] =
        (if (includeDiagonals) p.adjacent() else p.adjacentCardinal).map( pos => data(pos.y)(pos.x) )

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

    def getColumn(x: Int): Vector[Char] = (0 to maxY).map( data(x)(_)).toVector

    def getRow(y: Int): Vector[Char] = data(y).toVector

    def getDimensions: (Int, Int) = (maxX+1, maxY+1)

    def countOf(item: Char): Int = data.map(_.toList).toList.flatten.count( _ == item )
    
    def nextPoint(p: (Int, Int)): (Int, Int) = 
        if (p.x < maxX) (p.x + 1, p.y)
        else (0, p.y + 1)

    def isInsideGrid(p: (Int, Int)): Boolean = 0 <= p.x && p.x <= maxX && 0 <= p.y && p.y <= maxY

    // TODO: need also indexToPos
    def posToIndex(pos: (Int, Int)): Int = pos.y * maxY + pos.x

    def getAdjacentArea(point: (Int, Int)): Vector[(Int, Int)] = {
        val visited = ArrayBuffer(point)
        val queue = util.ArrayDeque[(Int, Int)]()
        queue.add(point)
        while !queue.isEmpty do {
            val current = queue.removeFirst()
            current.adjacentCardinal.filter( p => isInsideGrid(p) && data(p.y)(p.x) == data(point.y)(point.x) )
                .foreach ( connection =>
                    if !visited.contains(connection) then {
                        visited += connection
                        queue.add(connection)
                    }
            )
        }
        visited.toVector
    }

    def findAllAreas: Vector[(Char, Vector[(Int, Int)])] = {
        val allPoints = getAllCoordinates
        val result = ArrayBuffer[(Char, Vector[(Int, Int)])]()
        val processed = ArrayBuffer[(Int, Int)]()
        for p <- allPoints do {
            if !processed.contains(p) then {
                val area = getAdjacentArea(p)
                result += ((data(p.y)(p.x), area))
                processed ++= area
            }
        }
        result.toVector
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
        val horizGroups = area.groupBy(_.y).map( (k, v) => (k, v.sortBy( _.x )) ).toVector.sortBy(_._1)
        val vertGroups = area.groupBy(_.x).map( (k, v) => (k, v.sortBy( _.y )) ).toVector.sortBy(_._1)
        countVertEdges(horizGroups) + countHorizEdges(vertGroups)
    }

    private def countVertEdges(ptsGrouped: Vector[(Int, Vector[(Int, Int)])]): Int = {
        var numberOfEdges = 0
        var prevEdges = Set[(Int, Direction)]()
        ptsGrouped.map( _._2 ).foreach(g =>
            val thisEdges = Set((g.head.x, W)) ++ Set((g.last.x + 1, E)) ++
                g.map(_.x).sliding(2).filter( x => x.size > 1 && x.head + 1 != x(1)).flatMap( x => Set((x.head + 1, E), (x(1), W)) ).toSet
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
        ptsGrouped.map( _._2 ).foreach(g =>
            val thisEdges = Set((g.head.y, N)) ++ Set((g.last.y + 1, S)) ++
                g.map( _.y ).sliding(2).filter( y => y.size > 1 && y.head + 1 != y(1)).flatMap( y => Set((y.head + 1, S), (y(1), N)) ).toSet
            numberOfEdges += (
                if prevEdges.isEmpty then thisEdges.size
                else thisEdges.size - thisEdges.intersect(prevEdges).size
            )
            prevEdges = Set.from(thisEdges)
        )
        numberOfEdges
    }

    override def toString: String = {
        val sb = StringBuilder()
        sb.append((for i <- data.indices yield f"${i % 100}%2d " + data(i).mkString).mkString("\n"))
        sb.append("\n   " + data.head.indices.map(x => if x % 10 == 0 then ((x / 10) % 10).toString else " ").mkString)
        sb.append("\n   " + data.head.indices.map(x => (x % 10).toString).mkString)
        sb.toString()
    }

    def stringWithoutRowColIndx: String = {
        val sb = StringBuilder()
        sb.append((for i <- data.indices yield data(i).mkString).mkString("\n"))
        sb.toString()
    }

    def printIt(): Unit = println(this.toString)
}

extension (p: (Int, Int)) {
    def adjacent(diagonally: Boolean = true): Vector[(Int, Int)] =
        if diagonally then Vector.from(allDirections.map( dir => p + dir.incr ))
        else Vector.from(allCardinal.map( dir => p + dir.incr ))

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

        def fromArrow(arrow: Char): Direction =
            Direction.values.find(d => d.symbol == arrow).getOrElse(throw AoCException(s"invalid direction [$arrow]"))
    }

    def compareYX(point1: (Int, Int), point2: (Int, Int)): Boolean = {
        (point1, point2) match
            case (p1, p2) if p1.y < p2.y => true
            case (p1, p2) if p1.y > p2.y => false
            case (p1, p2) => p1.x < p2.x
    }
}

