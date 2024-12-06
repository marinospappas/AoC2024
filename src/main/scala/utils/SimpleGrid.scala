package org.mpdev.scala.aoc2024
package utils

import scala.reflect.ClassTag

open class SimpleGrid[T:ClassTag](gridData: List[String], mapper: Map[Char,T] = SimpleGrid.allCharsDefMapper) {

    private val data: Array[Array[T]] = gridData.toArray.map (s => s.toCharArray.map (c => mapper(c)))
    private val maxX: Int = data(0).length - 1
    private val maxY: Int = data.length - 1
    
    def getDataPoint(p: (Int, Int)): T = data(p._2)(p._1)
    
    def setDataPoint(p: (Int, Int), t: T): Unit = data(p._2)(p._1) = t
    
    def getAdjacent(p: Point, includeDiagonals: Boolean = false): Set[Point] =
        (if (includeDiagonals) p.adjacent() else p.adjacentCardinal()).toSet
    
    def findFirst(d: T): (Int, Int) = {
        val index = data.map(_.toList).toList.flatten.indexOf(d)
        if index < 0 then (-1, -1)
        else {
            val y = index / (maxY + 1)
            (index - y * (maxX + 1), y)
        }
    }
    
    //def getColumn(x: Int): List[(Point,T)] = data.filter ( _._1.x == x ).toList

    //def getRow(y: Int): List[(Point,T)] = data.filter ( _._1.y == y ).toList

    def getDimensions: (Int, Int) = (maxX+1, maxY+1)

    def countOf(item: T): Int = data.map(_.toList).toList.flatten.count( _ == item )
    
    def nextPoint(p: (Int, Int)): (Int, Int) = 
        if (p._1 < maxX) (p._1 + 1, p._2)
        else (0, p._2 + 1)

    def isInsideGrid(p: (Int, Int)): Boolean = (0 to maxX).contains(p._1) && (0 to maxY).contains(p._2)
    
    /*
    private def data2Grid(): Array[Array[Char]] =
        val grid: Array[Array[Char]] = Array.fill(maxY-minY+1) { Array.fill(maxX-minX+1) { DEFAULT_CHAR } }
        for ( (pos, item) <- data) do grid(pos.y - minY)(pos.x - minX) = map2Char(item)
        grid

    private def map2Char(t: T) =
        mapper.map(_.swap).getOrElse(t, t match
            case i: Int => ('0'.toInt + i % 10).toChar
            case _ => 'x')

    def printIt(): Unit = printGrid(data2Grid())

    private def printGrid(grid: Array[Array[Char]]): Unit =
        for (i <- grid.indices)
            print(f"${i%100}%2d ")
            for (j <- grid.head.indices)
                print(grid(i)(j))
            println("")
        print("   ")
        for (i <- grid.head.indices)
            print(if (i%10 == 0) (i/10)%10 else " ")
        println("")
        print("   ")
        for (i <- grid.head.indices)
            print(i%10)
        println("")
        
     */
    
}

object SimpleGrid {
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

