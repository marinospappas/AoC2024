package org.mpdev.scala.aoc2024
package utils

import scala.collection.mutable
import scala.language.postfixOps


case class GridBuilder[T](gridData: Map[Point,T] = Map(),
                          mapper: Map[Char,T] = Map(),
                          border: Int = 0,
                          defaultChar: Char = '.',
                          defaultSize: (Int, Int) = (-1,-1),
                          printFormat: String = Grid.DEFAULT_FORMAT) {

    def withGridData(gridData: Map[Point,T]): GridBuilder[T] = copy(gridData = gridData)

    // IMPORTANT!! requires the mapper to have been set
    def fromVisualGrid(inputGridVisual: Vector[String]): GridBuilder[T] = 
        copy(gridData = processInputVisual(inputGridVisual, mapper))

    def fromPointsList(inputPoints: Vector[Point]): GridBuilder[T] = copy(gridData = processInputPoints(inputPoints))

    def fromXYListVisual(inputXYList: Vector[String]): GridBuilder[T] = copy(gridData = processInputXY(inputXYList))

    /* TODO create additional builder methods when needed
    def this(xyList: List[Point], mapper: Map[Char,T] = Map(), border: Int = 1, defaultChar: Char = '.', defaultSize: (Int,Int) = (-1,-1), function: Point => T) =
            this(mapper = mapper, border = border, defaultChar = defaultChar, defaultSize = defaultSize) {
        xyList.forEach ( p => data(p) = function(p) )
        updateXYDimensions(border)
    }

    def this(xRange: Range, yRange: Range, mapper: Map[Char,T] = Map(), border: Int = 1, defaultChar: Char = '.', defaultSize: (Int,Int) = (-1,-1), function: (Int,Int) => T) =
            this(mapper = mapper, border = border, defaultChar = defaultChar, defaultSize = defaultSize) {
        xRange.forEach ( x => yRange.forEach ( y => data(Point(x,y)) = function(x, y) ) )
        updateXYDimensions(border)
    }
     */
    
    def withMapper(mapper: Map[Char,T]): GridBuilder[T] = copy(mapper = mapper)

    def withBorder(border: Int): GridBuilder[T] = copy(border = border)

    def withDefaultChar(defaultChar: Char): GridBuilder[T] = copy(defaultChar = defaultChar)

    def withDefaultSize(defaultSize: (Int, Int)): GridBuilder[T] = copy(defaultSize = defaultSize)
    
    def withPrintFormat(printFormat: String): GridBuilder[T] = copy(printFormat = printFormat)

    def build(): Grid[T] = Grid[T](gridData, mapper, border, defaultChar, defaultSize, printFormat)

    // conversion of input data to Map[Point,T]
    private def processInputVisual(input: Vector[String], mapper: Map[Char, T]): Map[Point, T] =
        val thisMap = mutable.Map[Point, T]()
        for (y <- input.indices)
            for (x <- input(y).indices)
                if (mapper contains input(y)(x))
                    thisMap += (Point(x, y) -> mapper(input(y)(x)))
        thisMap.toMap
        
    private def processInputPoints(input: Vector[Point]): Map[Point, T] =
        val thisMap = mutable.Map[Point, T]()
        for (p <- input)
            thisMap += p -> mapper.values.head
        thisMap.toMap

    private def processInputXY(input: Vector[String]): Map[Point, T] =
        val thisMap = mutable.Map[Point, T]()
        for (s <- input)
            val coords = s.split(",")
            thisMap += Point(coords(0).trim().toInt, coords(1).trim().toInt) -> mapper.values.head
        thisMap.toMap
}

