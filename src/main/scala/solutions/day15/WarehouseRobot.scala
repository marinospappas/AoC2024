package org.mpdev.scala.aoc2024
package solutions.day15

import framework.{AocMain, InputReader, PuzzleSolver}
import solutions.day15.WarehouseRobot.{EMPTY, GOODS, GOODS_LEFT_EDGE, ROBOT, WALL, doubleWidth, moveRobot, moveRobot2}
import utils.{+, SimpleGrid, x, y, *}
import utils.SimpleGrid.Direction
import utils.SimpleGrid.Direction.*

import scala.math.min
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

    val warehouse2 = SimpleGrid(inputData.splitAt(inputData.indexOf(""))._1
        .map( _.toCharArray.flatMap(doubleWidth).mkString )
    )
    val robotPos2: (Int, Int) = warehouse2.findFirst(ROBOT)

    override def part2: Any = {
        moveRobot2(warehouse2, robotPos2, directions)
        println(warehouse2)
        warehouse2.findAll(GOODS_LEFT_EDGE).map( p => 100 * p.y + p.x ).sum
    }
}

object WarehouseRobot {
    val WALL = '#'
    val GOODS = 'O'
    val EMPTY = '.'
    val ROBOT = '@'
    val GOODS_LEFT_EDGE = '['
    val GOODS_RIGHT_EDGE = ']'

    private val otherEdge = Map(GOODS_LEFT_EDGE -> (1, 0), GOODS_RIGHT_EDGE -> (-1, 0)).withDefault(c => (0, 0))

    def doubleWidth(c: Char): String =
        c match
            case WALL => "##"
            case GOODS => "[]"
            case EMPTY => ".."
            case ROBOT => "@."

    def pushNarrowItems(grid: SimpleGrid, curPos: (Int, Int), direction: Direction): Boolean = {
        curPos + direction.incr match
            case p if grid.getDataPoint(p) == EMPTY =>
                grid.setDataPoint(p, grid.getDataPoint(curPos))
                grid.setDataPoint(curPos, EMPTY)
                true
            case p if grid.getDataPoint(p) == GOODS =>
                if pushNarrowItems(grid, p, direction) then
                    grid.setDataPoint(p, grid.getDataPoint(curPos))
                    grid.setDataPoint(curPos, EMPTY)
                    true
                else false
            case _ => false
    }

    def pushWideItems(grid: SimpleGrid, curPos: (Int, Int), direction: Direction): Boolean =
        if Set(E, W).contains(direction) then pushWideItemsHoriz(grid, curPos, direction)
        else pushWideItemsVert(grid, curPos, direction)

    def isEmpty(grid: SimpleGrid, point: (Int, Int), direction: Direction): Boolean = {
        direction match
            case d if Set(E, W).contains(d) => grid.getDataPoint(point + d.incr * 2) == EMPTY
            case d => // N, S
                grid.getDataPoint(point + direction.incr) == EMPTY &&
                    grid.getDataPoint((point + otherEdge(grid.getDataPoint(point))) + direction.incr) == EMPTY
    }

    def isWall(grid: SimpleGrid, point: (Int, Int), direction: Direction): Boolean = {
        direction match
            case d if Set(E, W).contains(d) => grid.getDataPoint(point + d.incr * 2) == WALL
            case d => // N, S
                grid.getDataPoint(point + direction.incr) == WALL ||
                    grid.getDataPoint((point + otherEdge(grid.getDataPoint(point))) + direction.incr) == WALL
    }

    private def pushWideItemsHoriz(grid: SimpleGrid, curPos: (Int, Int), direction: Direction): Boolean = {
        val goodsPos = (curPos, curPos + otherEdge(grid.getDataPoint(curPos)))
        goodsPos match
            case (p1, p2) if grid.getDataPoint(p2 + direction.incr) == EMPTY =>
                val goodsPos = (curPos, curPos + otherEdge(grid.getDataPoint(curPos)))
                grid.setDataPoint(p2 + direction.incr, grid.getDataPoint(p2))
                grid.setDataPoint(p1 +  direction.incr, grid.getDataPoint(p1))
                grid.setDataPoint(p1, EMPTY)
                true
            case (p1, p2) if grid.getDataPoint(p2 + direction.incr) == WALL => false
            case (p1, p2) =>
                if pushWideItemsHoriz(grid, p2 + direction.incr, direction) then
                    grid.setDataPoint(p2 + direction.incr, grid.getDataPoint(p2))
                    grid.setDataPoint(p1 + direction.incr, grid.getDataPoint(p1))
                    grid.setDataPoint(p1, EMPTY)
                    true
                else false
    }

    def pushWideItemsVert(grid: SimpleGrid, curPos: (Int, Int), direction: Direction): Boolean = {
        val goodsPos = (curPos, curPos + otherEdge(grid.getDataPoint(curPos)))
        goodsPos match
            case (p1, p2) if grid.getDataPoint(p1 + direction.incr) == EMPTY && grid.getDataPoint(p2 + direction.incr) == EMPTY =>
                grid.setDataPoint(p1 + direction.incr, grid.getDataPoint(p1))
                grid.setDataPoint(p2 + direction.incr, grid.getDataPoint(p2))
                grid.setDataPoint(p1, EMPTY)
                grid.setDataPoint(p2, EMPTY)
                true
            case (p1, p2) if grid.getDataPoint(p1 + direction.incr) == WALL || grid.getDataPoint(p2 + direction.incr) == WALL => false
            case (p1, p2) =>
                val canMoveP1 = grid.getDataPoint(p1 + direction.incr) == EMPTY || pushWideItemsVert(grid.clone, p1 + direction.incr, direction)
                val canMoveP2 = grid.getDataPoint(p2 + direction.incr) == EMPTY || pushWideItemsVert(grid.clone, p2 + direction.incr, direction)
                if canMoveP1 && canMoveP2 then
                    if grid.getDataPoint(p1 + direction.incr) != EMPTY then pushWideItemsVert(grid, p1 + direction.incr, direction)
                    if grid.getDataPoint(p2 + direction.incr) != EMPTY then pushWideItemsVert(grid, p2 + direction.incr, direction)
                    grid.setDataPoint(p1 + direction.incr, grid.getDataPoint(p1))
                    grid.setDataPoint(p2 + direction.incr, grid.getDataPoint(p2))
                    grid.setDataPoint(p1, EMPTY)
                    grid.setDataPoint(p2, EMPTY)
                    true
                else false
    }

    def moveRobot(grid: SimpleGrid, startPos: (Int, Int), directions: Vector[Direction]): Unit = {
        var curPos = startPos
        grid.setDataPoint(curPos, EMPTY)
        for d <- directions do {
            curPos + d.incr match
                case p if grid.getDataPoint(p) == WALL => ;
                case p if grid.getDataPoint(p) == GOODS => if pushNarrowItems(grid, p, d) then curPos = p
                case p => curPos = p
        }
        grid.setDataPoint(curPos, ROBOT)
    }

    def moveRobot2(grid: SimpleGrid, startPos: (Int, Int), directions: Vector[Direction]): Unit = {
        var curPos = startPos
        var n = 0
        for d <- directions do {
            grid.setDataPoint(curPos, EMPTY)
            curPos + d.incr match
                case p if grid.getDataPoint(p) == WALL => ;
                case p if Set(GOODS_LEFT_EDGE, GOODS_RIGHT_EDGE).contains(grid.getDataPoint(p)) => if pushWideItems(grid, p, d) then curPos = p
                case p => curPos = p
                
                case p if isEmpty(grid, p, d) => curPos = p + d.incr
                case p if isWall(grid, p, d) => ;
                case p => if pushWideItems(grid, p, d) then curPos = p + d.incr
            grid.setDataPoint(curPos, ROBOT)
            if AocMain.environment == "test" then println(n); println(grid.stringWithoutRowColIndx)
            n += 1
        }
    }
}
