package org.mpdev.scala.aoc2024
package solutions.day12

import framework.{AoCException, InputReader, PuzzleSolver}
import utils.SimpleGrid
import utils.SimpleGrid.Direction
import utils.SimpleGrid.Direction.*

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.boundary.break
import scala.util.{Failure, Success, boundary}

class FenceCalculator extends PuzzleSolver {

    val plotsGrid: SimpleGrid = SimpleGrid(InputReader.read(12))
    val allCoords: Vector[(Int, Int)] = plotsGrid.getAllCoordinates
    var allPlots: Vector[(Char, Vector[(Int, Int)])] = Vector()
    
    def findAllPlots(grid: SimpleGrid): Vector[(Char, Vector[(Int, Int)])] = {
        val allPoints = grid.getAllCoordinates
        val result = ArrayBuffer[(Char, Vector[(Int, Int)])]()
        val processed = ArrayBuffer[(Int, Int)]()
        for p <- allPoints do {
            if !processed.contains(p) then {
                val datum = grid.getDataPoint(p)
                val area = grid.getAdjacentArea(p)
                result += ((datum, area))
                processed ++= area
            }
        }
        result.toVector
    }

    override def part1: Any = {
        allPlots = findAllPlots(plotsGrid)
        allPlots.map( plot => (plot._1, plot._2.size, plotsGrid.getPerimeter(plot._2)))
            .foldLeft(0)( (sum, cur) => sum + cur._2 * cur._3 )
    }

    override def part2: Any = {
        ""
    }
}
