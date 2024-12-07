package org.mpdev.scala.aoc2024
package solutions.day06

import framework.{AoCException, InputReader, PuzzleSolver}
import utils.SimpleGrid
import utils.SimpleGrid.Direction
import utils.SimpleGrid.Direction.*

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.boundary
import scala.util.boundary.break
import scala.util.{Failure, Success}

class MapRouteExplorer extends PuzzleSolver {

    val mapGrid: SimpleGrid[Char] = SimpleGrid(InputReader.read(6))
    val (guard, obstacle, obstruction, empty, trace) = ('S', '#', 'O', '.', 'X')
    val startPosition: (Int, Int) = mapGrid.findFirst(guard)
    private val direction = N
    private val NUM_THREADS = 4

    // returns false if loop detected
    private def walkMap(grid: SimpleGrid[Char], start: (Int, Int, Direction),
                        route: ArrayBuffer[(Int, Int, Direction)], obstaclePoint: (Int, Int) = (-1,-1)): Boolean = {
        var (curX, curY, facing) = start
        // a temporary map is used to build the route so that the lookup for previous points in the route can be done fast (as key lookup)        
        val thisRoute = mutable.Map[(Int, Int, SimpleGrid.Direction), Int]()
        var routeIndex = 0
        boundary:
            while grid.getDataPoint((curX, curY)) != null do {
                if thisRoute.contains((curX, curY, facing)) then
                    break(false) // loop detected
                var nextPosition = (curX + facing.incr._1, curY + facing.incr._2)
                while  grid.getDataPoint(nextPosition) == obstacle || nextPosition == obstaclePoint do {
                    facing = facing.turnRight
                    nextPosition = (curX + facing.incr._1, curY + facing.incr._2)
                }
                thisRoute.put((curX, curY, facing), routeIndex)
                routeIndex += 1
                curX = nextPosition._1
                curY = nextPosition._2
            }
            // now update the real route
            thisRoute.toList.sortBy(_._2).foreach(route += _._1)
            true
    }

    private def identifyObstructionPoints(threadId: Int, route: List[(Int, Int, Direction)]): List[(Int, Int)] = {
        println(s">>t$threadId")
        val obstructions = ArrayBuffer[(Int, Int)]()
        for i <- 1 until route.size do {
            if i % NUM_THREADS == threadId then {     // only pickup those points that are for this thread - based on %
                val curPos = (route(i)._1, route(i)._2)
                if guardRoutePoints.indexOf(curPos) == i then {
                    if !walkMap(mapGrid, route(i - 1), ArrayBuffer[(Int, Int, Direction)]() ++ route.slice(0, i - 1), curPos) then
                        obstructions += curPos
                }
            }
        }
        println(s"t$threadId<<")
        obstructions.toList
    }

    var guardRoute: ArrayBuffer[(Int, Int, Direction)] = ArrayBuffer()
    private var guardRoutePoints: List[(Int, Int)] = List()

    override def part1: Any = {
        if !walkMap(mapGrid, (startPosition._1, startPosition._2, N), guardRoute) then throw AoCException("Day 6 - no solution found")
        guardRoute.foreach (r => if (r._1, r._2) != startPosition then mapGrid.setDataPoint((r._1, r._2), r._3.symbol))
        guardRoutePoints = guardRoute.map (r => (r._1, r._2)).toList
        guardRoutePoints.distinct.size
    }

    var obstructions: List[(Int, Int)] = List()

    // multi threading brute force for performance
    override def part2: Any = {
        val futures: Array[Future[List[(Int, Int)]]] = Array.fill(NUM_THREADS) {null}
        for i <- 0 until NUM_THREADS do
            futures(i) = Future(identifyObstructionPoints(i, guardRoute.toList))
        val result = for
                res1 <- futures(0)
                res2 <- futures(1)
                res3 <- futures(2)
                res4 <- futures(3)
        yield
                res1 ++ res2 ++ res3 ++ res4
        while futures.exists(!_.isCompleted) do { Thread.sleep(10); print(".") }
        result.onComplete {
            case Success(x) =>
                println()
                obstructions = x
            case Failure(e) =>
                e.printStackTrace()
        }
        obstructions.foreach (o => mapGrid.setDataPoint(o, obstruction))
        obstructions.distinct.size
    }
}
