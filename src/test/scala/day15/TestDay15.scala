package org.mpdev.scala.aoc2024
package day15

import framework.{AocMain, InputReader}
import solutions.day15.WarehouseRobot
import solutions.day15.WarehouseRobot.pushItems
import utils.SimpleGrid
import utils.SimpleGrid.Direction.*

import utils.SimpleGrid.Direction
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay15 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = WarehouseRobot()

    it should "read input and setup warehouse and directions" in {
        println(solver.warehouse)
        println(solver.directions)
        println(solver.robotPos)
        (solver.warehouse.getDimensions, solver.robotPos, solver.directions.size) shouldBe ((10, 10), (4,4), 700)
    }

    private val w1 = Vector(
        "########",
        "#.@O.O.#",
        "##..O..#",
        "#...O..#",
        "#.#.O..#",
        "#...O..#",
        "#......#",
        "########"
    )
    it should "push items around the warehouse" in {
        val grid = SimpleGrid(w1)
        println(grid)
        println(WarehouseRobot.pushItems(grid, (3, 1), E))
        println(grid)
        println(WarehouseRobot.pushItems(grid, (4, 1), E))
        println(grid)
        println(WarehouseRobot.pushItems(grid, (4, 2), S))
        println(grid)
    }

    private val afterShifting1 = Vector(
        "########",
        "#....OO#",
        "##.....#",
        "#.....O#",
        "#.#O@..#",
        "#...O..#",
        "#...O..#",
        "########"
    )
    it should "move around the warehouse while pushing goods" in {
        val grid = SimpleGrid(w1)
        val directions = "<^^>>>vv<v>>v<<".map( Direction.fromArrow ).toVector
        println(grid)
        WarehouseRobot.moveRobot(grid, (2, 1), directions)
        println(grid)
        grid.getDataPoints shouldBe SimpleGrid(afterShifting1).getDataPoints
    }

    private val afterShifting2 = Vector(
        "##########",
        "#.O.O.OOO#",
        "#........#",
        "#OO......#",
        "#OO@.....#",
        "#O#.....O#",
        "#O.....OO#",
        "#O.....OO#",
        "#OO....OO#",
        "##########"
    )
    it should "solve part1 correctly" in {
        val result = solver.part1
        println(solver.warehouse)
        (result, solver.warehouse.getDataPoints) shouldBe (10092, SimpleGrid(afterShifting2).getDataPoints)
    }
    
}
