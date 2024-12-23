package org.mpdev.scala.aoc2024
package utils

import utils.Grid.*

import java.util
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

open class Grid[T](gridData: Map[Point,T],
                   mapper: Map[Char,T] = Grid.allCharsDefMapper,
                   borderWidth: Int = 0,
                   defaultChar: Char = '.',
                   defaultSize: (Int, Int) = (-1,-1),
                   printFormat: String = DEFAULT_FORMAT) {

    private val data: mutable.Map[Point, T] = mutable.Map[Point, T]()
    private var maxX: Int = 0
    private var maxY: Int = 0
    private var minX: Int = 0
    private var minY: Int = 0
    private var cornerPoints: Set[Point] = Set()
    private var border: List[Point] = List()

    {
        data ++= gridData
        updateXYDimensions(borderWidth)
        DEFAULT_CHAR = defaultChar
    }

    def fill(datum: T): Unit = for (key <- data.keys) do data += key -> datum

    def getDataPoints: Map[Point,T] = data.toMap

    def setDataPoints(dataPoints: Map[Point,T]): Unit = data.empty ++ dataPoints

    def getDataPoint(p: Point): T | Null = data.getOrElse(p, null)

    def getDataPointOptional(p: Point): Option[T] = data.get(p)

    def setDataPoint(p: Point, t: T): Unit = data += p -> t

    def containsDataPoint(p: Point): Boolean = data.contains(p)

    def getAdjacent(p: Point, includeDiagonals: Boolean = false): Set[Point] =
        (if (includeDiagonals) p.adjacent() else p.adjacentCardinal()).toSet

    def removeDataPoint(p: Point): Unit =
        data -= p

    def findFirstOrNull(d: T): Point = data.map(_.swap).getOrElse(d, null)

    def findFirst(d: T): Point = findFirstOrNull(d)

    def getColumn(x: Int): List[(Point,T)] = data.filter ( _._1.x == x ).toList

    def getRow(y: Int): List[(Point,T)] = data.filter ( _._1.y == y ).toList
    
    def getDimensions: (Int, Int) = (maxX-minX+1, maxY-minY+1)
    def getMinMaxXY: (Int, Int, Int, Int) = (minX, maxX, minY, maxY)
    def getCorners: Set[Point] = cornerPoints
    def getBorder: List[Point] = border

    def countOf(item: T): Int = data.count( _._2 == item )

    def firstPoint(): Point = Point(minX, minY)

    def nextPoint(p: Point): Point = if (p.x < maxX) p + Point(1,0) else Point(minX,p.y+1)

    def isInsideGrid(p: Point): Boolean = (minX to maxX).contains(p.x) && (minY to maxY).contains(p.y)

    def isOnBorder(p: Point): Boolean =
        Set(minX, maxX).contains(p.x) && (minY to maxY).contains(p.y) ||
            (minX to maxX).contains(p.x) && Set(minY, maxY).contains(p.y)
        
    def updateDimensions(): Unit = updateXYDimensions(borderWidth)

    def getRowAsList(n: Int): List[T] = data.filter(_._1.y == n).values.toList

    def getColAsList(n: Int): List[T] = data.filter(_._1.x == n).values.toList

    def setRowFromList(n: Int, rowData: List[T]): Unit =
        for (x <- rowData.indices)
            data(Point(minX + x, n)) = rowData(x)

    // mapping of a column or a row to int by interpreting the co-ordinates as bit positions
    def mapRowToInt(n: Int, predicate: T => Boolean = { _ => true } ): Int =
        data.filter( e => predicate(e._2) && e._1.y == n ).map( e => Grid.bitToInt(e._1.x) ).sum

    def mapColToInt(n: Int, predicate: T => Boolean = { _ => true }): Int =
        data.filter( e => predicate(e._2) && e._1.x == n ).map ( e => Grid.bitToInt(e._1.y) ).sum

    private def updateXYDimensions(borderWidth: Int): Unit = {
        if (defaultSize._1 > 0 && defaultSize._2 > 0)
            minX = 0
            maxX = defaultSize._1 - 1
            minX = 0
            maxY = defaultSize._2 - 1
        else if (data.nonEmpty)
            maxX = data.keys.map(_.x).max + borderWidth
            maxY = data.keys.map(_.y).max + borderWidth
            minX = data.keys.map(_.x).min - borderWidth
            minY = data.keys.map(_.y).min - borderWidth
        else
            maxX = 0
            maxY = 0
            minX = 0
            minY = 0
        cornerPoints = Set(Point(minX, minY), Point(minX, maxY), Point(maxX, minY), Point(maxX, maxY))
        val gridBorder = ArrayBuffer[Point]()
        (minX to maxX).foreach(x => gridBorder += Point(x, minY))
        (minY + 1 to maxY).foreach(y => gridBorder += Point(maxX, y))
        (maxX - 1 to minX by -1).foreach(x => gridBorder += Point(x, maxY))
        (maxY - 1 to minY + 1 by -1).foreach(y => gridBorder += Point(minX, y))
        border = gridBorder.toList
    }

    def getAdjacentArea(point: Point): Vector[Point] = {
        val visited = ArrayBuffer(point)
        val queue = util.ArrayDeque[Point]()
        queue.add(point)
        while !queue.isEmpty do {
            val current = queue.removeFirst()
            current.adjacentCardinal().filter(p => getDataPoint(p) == data(point))
                .foreach(connection =>
                    if !visited.contains(connection) then {
                        visited += connection
                        queue.add(connection)
                    }
                )
        }
        visited.toVector
    }

    def findAllAreas: Vector[(T, Vector[Point])] = {
        val result = ArrayBuffer[(T, Vector[Point])]()
        val processed = ArrayBuffer[Point]()
        for p <- data.keys do {
            if !processed.contains(p) then {
                val area = getAdjacentArea(p)
                result += ((data(p), area))
                processed ++= area
            }
        }
        result.toVector
    }

    private def data2Grid(): Array[Array[String]] =
        val grid: Array[Array[String]] = Array.fill(maxY-minY+1) { Array.fill(maxX-minX+1) { DEFAULT_CHAR.toString } }
        for ( (pos, item) <- data) do grid(pos.y - minY)(pos.x - minX) = printFormat.format(map2Char(item))
        grid

    private def map2Char(t: T) =
        mapper.map(_.swap).getOrElse(t, t match
            case i: Int => ('0'.toInt + i % 10).toChar
            case _ => 'x')

    override def toString: String = {
        val stringGrid = data2Grid()
        val sb = StringBuilder()
        for (i <- stringGrid.indices)
            sb.append(f"${i % 100}%2d ")
            for (j <- stringGrid.head.indices)
                sb.append(stringGrid(i)(j))
            sb.append("\n")
        sb.append("   ")
        for (i <- stringGrid.head.indices)
            sb.append(if (i % 10 == 0) (i / 10) % 10 else " ")
        sb.append("\n")
        sb.append("   ")
        for (i <- stringGrid.head.indices)
            sb.append(printFormat.format((i % 10).toString.head))
        sb.append("\n")
        sb.toString
    }
    
    def printIt(): Unit = printGrid(data2Grid())

    private def printGrid(grid: Array[Array[String]]): Unit = {
        for (i <- grid.indices)
            print(f"${i % 100}%2d ")
            for (j <- grid.head.indices)
                print(grid(i)(j))
            println("")
        print("   ")
        for (i <- grid.head.indices)
            print(if (i % 10 == 0) (i / 10) % 10 else " ")
        println("")
        print("   ")
        for (i <- grid.head.indices)
            print(printFormat.format((i % 10).toString.head))
        println("")
    }
}

object Grid {
    val DEFAULT_FORMAT = "%c"
    var DEFAULT_CHAR = '.'
    val allCharsDefMapper: Map[Char, Char] = (' ' to '~').map(c => c -> c).toMap
    val bitToInt: Array[Int] = Array(1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768,
        65536, 131072, 262144, 524288, 1_048_576, 2_097_152, 4_194_304, 8_388_608,
        16_777_216, 33_554_432, 67_108_864, 134_217_728, 268_435_456, 536_870_912, 1_073_741_824)
}

