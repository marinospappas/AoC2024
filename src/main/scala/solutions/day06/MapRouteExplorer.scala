package org.mpdev.scala.aoc2024
package solutions.day06

import framework.{AoCException, InputReader, PuzzleSolver}
import utils.SimpleGrid
import utils.SimpleGrid.Direction.*

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import scala.util.boundary.break

class MapRouteExplorer extends PuzzleSolver {

    val mapGrid: SimpleGrid[Char] = SimpleGrid(InputReader.read(6))
    val (guard, obstacle, obstruction, empty, trace) = ('S', '#', 'O', '.', 'X')
    val startPosition: (Int, Int) = mapGrid.findFirst(guard)
    private val direction = N
    var iterations = 0

    // returns false if loop detected
    private def walkMap(grid: SimpleGrid[Char], start: (Int, Int, SimpleGrid.Direction),
                        route: ArrayBuffer[(Int, Int, SimpleGrid.Direction)], obstaclePoint: (Int, Int) = (-1,-1)): Boolean = {
        var (curX, curY, facing) = start
        boundary:
            while grid.isInsideGrid((curX, curY)) do {
                iterations += 1
                if route.contains((curX, curY, facing)) then
                    break(false) // loop detected
                var nextPosition = (curX + facing.incr._1, curY + facing.incr._2)
                while  grid.isInsideGrid(nextPosition) && grid.getDataPoint(nextPosition) == obstacle || nextPosition == obstaclePoint do {
                    facing = facing.turnRight
                    nextPosition = (curX + facing.incr._1, curY + facing.incr._2)
                }
                route += ((curX, curY, facing))
                curX = nextPosition._1
                curY = nextPosition._2
            }
            true
    }

    private def identifyObstructionPoints: List[(Int, Int)] = {
        val triedAlready = ArrayBuffer[(Int, Int)]()
        val obstructions = ArrayBuffer[(Int, Int)]()
        for i <- 1 until guardRoute.size do {
            if i % 10 == 0 then print('.')
            val curPos = (guardRoute(i)._1, guardRoute(i)._2)
            val curRoute = guardRoute.slice(0, i - 1)
            if !triedAlready.contains(curPos) then {
                triedAlready += curPos
                if !walkMap(mapGrid, guardRoute(i - 1), ArrayBuffer[(Int, Int, SimpleGrid.Direction)]() ++ curRoute, curPos) then
                    obstructions += curPos
            }
        }
        println()
        obstructions.toList
    }

    var guardRoute: ArrayBuffer[(Int, Int, SimpleGrid.Direction)] = ArrayBuffer()

    override def part1: Any = {
        if !walkMap(mapGrid, (startPosition._1, startPosition._2, N), guardRoute) then throw AoCException("Day 6 - no solution found")
        guardRoute.foreach (r => if (r._1, r._2) != startPosition then mapGrid.setDataPoint((r._1, r._2), trace)) // MapObject.ofDirection(r._3)))
        guardRoute.map (r => (r._1, r._2)).distinct.size
    }

    var obstructions: List[(Int, Int)] = List()

    override def part2: Any = {
        obstructions = identifyObstructionPoints
        obstructions.foreach (o => mapGrid.setDataPoint(o, obstruction))
        println(s"iterations: $iterations")
        obstructions.distinct.size
    }
}
