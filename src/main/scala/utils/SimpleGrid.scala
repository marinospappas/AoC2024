package org.mpdev.scala.aoc2024
package utils

import scala.reflect.ClassTag

open class SimpleGrid(gridData: List[String]) {

    private val data: Array[Array[Char]] = gridData.toArray.map (s => s.toCharArray)
    private val maxX: Int = data(0).length - 1
    private val maxY: Int = data.length - 1
    
    def getDataPoint(p: (Int, Int)): Char | Null =
        if isInsideGrid(p) then data(p._2)(p._1) else null
    
    def setDataPoint(p: (Int, Int), d: Char): Unit = data(p._2)(p._1) = d

    def getDataPoints: Array[Array[Char]] = data

    def getDataValues: Set[Char] = data.flatten.toSet

    def getAdjacent(p: Point, includeDiagonals: Boolean = false): Set[Point] =
        (if (includeDiagonals) p.adjacent() else p.adjacentCardinal()).toSet
    
    def findFirst(d: Char): (Int, Int) = {
        var seq = IndexedSeq[(Int, Int)]()
        if { seq = for {
                y <- 0 to maxY
                x <- 0 to maxX
                if data(y)(x) == d
            } yield (x, y)
            seq.nonEmpty } then seq(0) else (-1, -1)
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

    def printIt(): Unit = {
        for (i <- data.indices)
            (f"${i % 100}%2d " + data(i).mkString).printLn()
        ("   " + data.head.indices.map( x => if x % 10 == 0 then ((x / 10) % 10).toString else " " ).mkString).printLn()
        ("   " + data.head.indices.map( x => (x % 10).toString ).mkString).printLn()
    }
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
}

