package org.mpdev.scala.aoc2024
package solutions.day06

import framework.{AoCException, InputReader, PuzzleSolver}
import utils.{Grid, GridBuilder, GridUtils, Point}
import utils.GridUtils.Direction.*

import MapObject.*

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import scala.util.boundary.break

class MapRouteExplorer extends PuzzleSolver {

    val mapGrid: Grid[MapObject] = GridBuilder[MapObject]().withMapper(MapObject.mapper).fromVisualGrid(InputReader.read(6)).build()
    val startPosition: Point = mapGrid.findFirst(GUARD)
    private val direction = UP

    // returns false if loop detected
    private def walkMap(grid: Grid[MapObject], start: (Point, GridUtils.Direction), route: ArrayBuffer[(Point, GridUtils.Direction)], obstaclePoint: Point = Point(-1,-1)): Boolean = {
        var (curPos, facing) = start
        boundary:
            while grid.getDataPoint(curPos) != null do {
                if route.contains((curPos, facing)) then
                    break(false) // loop detected
                var nextPosition = curPos + facing.increment
                while grid.getDataPoint(nextPosition) == OBSTACLE || nextPosition == obstaclePoint do {
                    facing = facing.turnRight
                    nextPosition = curPos + facing.increment
                }
                route += ((curPos, facing))
                curPos = nextPosition
            }
            true
    }

    private def identifyObstructionPoints: List[Point] = {
        val triedAlready = ArrayBuffer[Point]()
        val obstructions = ArrayBuffer[Point]()
        for i <- 1 until guardRoute.size do {
            if i % 10 == 0 then print('.')
            val (curPos, curDir) = guardRoute(i)
            val curRoute = guardRoute.slice(0, i - 1)
            val potentialObstrPoint = curPos
            if !triedAlready.contains(potentialObstrPoint) then {
                triedAlready += potentialObstrPoint
                if !walkMap(mapGrid, guardRoute(i - 1), ArrayBuffer[(Point, GridUtils.Direction)]() ++ curRoute, potentialObstrPoint) then
                    obstructions += potentialObstrPoint
            }
        }
        println()
        obstructions.toList
    }

    var guardRoute: ArrayBuffer[(Point, GridUtils.Direction)] = ArrayBuffer()

    override def part1: Any = {
        if !walkMap(mapGrid, (startPosition, UP), guardRoute) then throw AoCException("Day 6 - no solution found")
        guardRoute.foreach (r => if r._1 != startPosition then mapGrid.setDataPoint(r._1, MapObject.ofDirection(r._2)))
        guardRoute.map (_._1).distinct.size
    }

    var obstructions: List[Point] = List()

    override def part2: Any = {
        obstructions = identifyObstructionPoints
        obstructions.foreach (o => mapGrid.setDataPoint(o, OBSTRUCTION))
        obstructions.distinct.size
    }
}
