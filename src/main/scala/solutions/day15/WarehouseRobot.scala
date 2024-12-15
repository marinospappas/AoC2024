package org.mpdev.scala.aoc2024
package solutions.day15

import framework.{InputReader, PuzzleSolver}
import solutions.day15.WarehouseRobot.{EMPTY, GOODS, ROBOT, WALL, moveRobot}
import utils.{+, SimpleGrid, x, y}
import utils.SimpleGrid.Direction
import utils.SimpleGrid.Direction.*

import scala.util.boundary
import scala.util.boundary.break

class WarehouseRobot extends PuzzleSolver {

    val inputData: Vector[String] = InputReader.read(15)
    val warehouse: SimpleGrid = SimpleGrid(inputData.splitAt(inputData.indexOf(""))._1)
    val directions: Vector[Direction] = inputData.splitAt(inputData.indexOf(""))._2.flatten.map( Direction.fromArrow )
    val robotPos: (Int, Int) = warehouse.findFirst(ROBOT)

    override def part1: Any = {
        moveRobot(warehouse, robotPos, directions)
        warehouse.findAll(GOODS).map( p => 100 * p.y + p.x ).sum
    }


    override def part2: Any =
        ""
}

object WarehouseRobot {
    val WALL = '#'
    val GOODS = 'O'
    val EMPTY = '.'
    val ROBOT = '@'

    def pushItems(grid: SimpleGrid, curPos: (Int, Int), direction: Direction): Boolean = {
        curPos + direction.incr match
            case p if grid.getDataPoint(p) == EMPTY =>
                grid.setDataPoint(p, grid.getDataPoint(curPos))
                grid.setDataPoint(curPos, EMPTY)
                true
            case p if grid.getDataPoint(p) == GOODS =>
                if pushItems(grid, p, direction) then
                    grid.setDataPoint(p, grid.getDataPoint(curPos))
                    grid.setDataPoint(curPos, EMPTY)
                    true
                else false
            case _ => false
    }

    def moveRobot(grid: SimpleGrid, startPos: (Int, Int), directions: Vector[Direction]): Unit = {
        var curPos = startPos
        grid.setDataPoint(curPos, EMPTY)
        for d <- directions do {
            curPos + d.incr match
                case p if grid.getDataPoint(p) == WALL => ;
                case p if grid.getDataPoint(p) == GOODS => if pushItems(grid, p, d) then curPos = p
                case p => curPos = p
        }
        grid.setDataPoint(curPos, ROBOT)
    }
}
