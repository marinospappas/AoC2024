package org.mpdev.scala.aoc2024
package solutions.day10

import framework.{AocMain, InputReader, PuzzleSolver}
import utils.{Bfs, Graph, Point, SimpleGrid}
import solutions.day10.TrailFinder.{TRAIL_END, TRAIL_START, initGraph}

class TrailFinder(var testData: List[String] = List()) extends PuzzleSolver {

    val grid: SimpleGrid = SimpleGrid(if (testData.nonEmpty) testData else InputReader.read(10))
    val graph: Graph[(Int, Int, Char)] = initGraph(grid)
    private val startPoints = grid.findAll(TRAIL_START)
    private val bfs: Bfs[(Int, Int, Char)] = Bfs(graph)

    override def part1: Any = {
        val paths = startPoints.map(p => (p._1, p._2, TRAIL_START))
            .flatMap(start => bfs.allPaths(start, node => node._3 == TRAIL_END))
            .map( p => (p.head, p.last) )
            .toList.sortBy(_._1)
        if AocMain.environment != "prod" then paths.foreach(println)
        paths.size
    }

    override def part2: Any = {
        val paths = startPoints.map(p => (p._1, p._2, TRAIL_START))
            .flatMap(start => bfs.allPaths(start, node => node._3 == TRAIL_END))
            .toList.map( p => (p.head, p.last) )
        if AocMain.environment != "prod" then paths.foreach(println)
        paths.size
    }
}

object TrailFinder {
    val TRAIL_START = '0'
    val TRAIL_END = '9'
    private val TRAIL: Seq[Char] = '0' to '9'

    // initialize the Graph
    private def initGraph(grid: SimpleGrid): Graph[(Int, Int, Char)] = {
        val graph = Graph[(Int, Int, Char)]()
        val (xSize, ySize) = grid.getDimensions
        for x <- 0 until xSize do
            for y <- 0 until ySize do {
                val datum = grid.getDataPoint((x, y))
                val node = (x, y, datum)
                if TRAIL.contains(datum) then {
                    val neighbours = grid.getAdjacent(Point(x, y))
                        .filter(pos => grid.isInsideGrid((pos.x, pos.y)))
                        .filter(pos => TRAIL.contains(grid.getDataPoint((pos.x, pos.y)))
                            && grid.getDataPoint((pos.x, pos.y)) == datum + 1)
                        .map(pos => (pos.x, pos.y, grid.getDataPoint((pos.x, pos.y))))
                    graph.addNodes(node, neighbours)
                    neighbours.foreach(n => graph.addNode(n))
                }
            }
        graph
    }
}
