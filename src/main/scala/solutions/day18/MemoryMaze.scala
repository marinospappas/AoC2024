package org.mpdev.scala.aoc2024
package solutions.day18

import framework.{InputReader, PuzzleSolver}
import utils.{Dijkstra, Graph, GraphBuilder, Grid, GridBuilder, Point}
import solutions.day18.MemoryMaze.{GRID_DIMENSIONS, NUM_OF_CORRUPTED, getNeighbours}
import solutions.day18.MemoryState.{CORRUPTED, PATH}

class MemoryMaze extends PuzzleSolver {

    val inputData: Vector[String] = InputReader.read(18)
    val grid: Grid[MemoryState] = GridBuilder[MemoryState]().withMapper(MemoryState.mapper)
        .fromXYListVisual(inputData.take(NUM_OF_CORRUPTED))
        .withDefaultSize(GRID_DIMENSIONS)
        .build()
    val graph: Graph[Point] = GraphBuilder[Point]().withCustomGetConnected(p => getNeighbours(p, grid)).build()
    val start: Point = Point(0,0)
    val end: Point = Point(GRID_DIMENSIONS._1 - 1, GRID_DIMENSIONS._2 - 1)

    override def part1: Any = {
        val dijkstra = Dijkstra[Point](graph)
        val minPath = dijkstra.minPath(start, p => p == end)
        minPath.foreach(p => grid.setDataPoint(p._1, PATH))
        println(s"part 1 - Dijkstra iterations: ${dijkstra.iterations}")
        minPath.head._2
    }

    override def part2: Any = {
        var memIndex = NUM_OF_CORRUPTED
        var startIndx = NUM_OF_CORRUPTED + 1
        var endIndx = inputData.length - 1
        var invocations = 0
        var iterations = 0
        while startIndx < endIndx do {    // binary search
            val midIndex = (startIndx + endIndx) / 2
            val newGrid = GridBuilder[MemoryState]().withGridData(grid.getDataPoints).withMapper(MemoryState.mapper)
                .withDefaultSize(GRID_DIMENSIONS).build()
            for p <- (NUM_OF_CORRUPTED + 1 to midIndex).map(i => Point.from(inputData(i))) do
                newGrid.setDataPoint(p, CORRUPTED)
            val newGraph = GraphBuilder[Point]().withCustomGetConnected(p => getNeighbours(p, newGrid)).build()
            val dijkstra = Dijkstra[Point](newGraph)
            if dijkstra.minPath(start, p => p == end).nonEmpty then
                startIndx = midIndex + 1
            else
                endIndx = midIndex
            invocations += 1
            iterations += dijkstra.iterations
        }
        println(s"part 2 - Dijkstra invocations: $invocations, total iterations: $iterations")
        inputData(endIndx)
    }
}

object MemoryMaze {

    var NUM_OF_CORRUPTED: Int = 1024
    var GRID_DIMENSIONS: (Int, Int) = (71, 71)

    def getNeighbours(point: Point, grid: Grid[MemoryState]): Set[(Point, Int)] =
        point.adjacentCardinal().filter( p => grid.isInsideGrid(p) && grid.getDataPoint(p) != CORRUPTED)
            .map((_, 1)).toSet
}
