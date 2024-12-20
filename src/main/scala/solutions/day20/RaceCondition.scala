package org.mpdev.scala.aoc2024
package solutions.day20

import framework.{InputReader, PuzzleSolver}
import solutions.day20.RaceCondition.{END, SHORTCUT_CRITERIA, START, fromGrid, getPointsList}
import utils.{Graph, SimpleGrid, adjacentCardinal, manhattan}

import java.util
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class RaceCondition extends PuzzleSolver {

    private val inputData = InputReader.read(20)
    val grid: SimpleGrid = SimpleGrid(inputData)
    val start: (Int, Int) = grid.findFirst(START)
    private val end = grid.findFirst(END)
    val graph: Graph[(Int, Int)] = fromGrid(grid, start)
    val route: Vector[(Int, Int)] = getPointsList(graph, start).toVector

    override def part1: Any = {
        (for i <- 0 to route.length - 4 yield
                for j <- i + 3 until route.length if route(j).manhattan(route(i)) == 2 && j - i - 2 >= SHORTCUT_CRITERIA
                    yield j - i - 2
            ).flatten.size
    }

    override def part2: Any = {
        (for i <- 0 to route.length - 2 yield
                for j <- i + 2 until route.length
                    if route(j).manhattan(route(i)) <= 20 && route(j).manhattan(route(i)) >= 2
                        && j - i - route(j).manhattan(route(i)) >= SHORTCUT_CRITERIA
                        yield ((route(i), route(j)), j - i - route(j).manhattan(route(i)))
            ).flatten.groupBy(_._1).size
    }
}

object RaceCondition {
    val WALL = '#'
    val PATH = '.'
    val START = 'S'
    val END = 'E'
    var SHORTCUT_CRITERIA = 100

    // convert non-directional graph to a list of points (each graph node has only 1 connected node)
    def getPointsList(graph: Graph[(Int, Int)], start: (Int, Int)): List[(Int, Int)] = {
        val points = ArrayBuffer[(Int, Int)](start)
        var current = start
        while graph.getConnected(current).nonEmpty do {
            val next = graph.getConnected(current).iterator.next()._1
            points += next
            current = next
        }
        points.toList
    }

    // convert grid to non-directional graph
    def fromGrid(grid: SimpleGrid, start: (Int, Int)): Graph[(Int, Int)] = {
        val graph = Graph[(Int, Int)]()
        val processed = mutable.Set[(Int, Int)](start)
        val queue = util.ArrayDeque[(Int, Int)]()
        queue.add(start)
        while !queue.isEmpty do {
            val current = queue.poll()
            current.adjacentCardinal.filter(p => grid.getDataPoint(p) == PATH || grid.getDataPoint(p) == END).foreach( connected =>
                if !processed.contains(connected) then {
                    graph.addNode(current, connected)
                    processed.add(connected)
                    queue.add(connected)
                }
            )
        }
        graph.addNode(grid.findFirst(END))
        graph
    }
}
