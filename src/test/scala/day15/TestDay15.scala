package org.mpdev.scala.aoc2024
package day15

import framework.{AocMain, InputReader}
import solutions.day15.WarehouseRobot
import solutions.day15.WarehouseRobot.{ROBOT, pushNarrowItems}
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
        println(WarehouseRobot.pushNarrowItems(grid, (3, 1), E))
        println(grid)
        println(WarehouseRobot.pushNarrowItems(grid, (4, 1), E))
        println(grid)
        println(WarehouseRobot.pushNarrowItems(grid, (4, 2), S))
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

    private val w2expected = Vector(
        "####################",
        "##....[]....[]..[]##",
        "##............[]..##",
        "##..[][]....[]..[]##",
        "##....[]@.....[]..##",
        "##[]##....[]......##",
        "##[]....[]....[]..##",
        "##..[][]..[]..[][]##",
        "##........[]......##",
        "####################"
    )
    it should "setup warehouse for wide goods" in {
        println(solver.warehouse2)
        println(solver.robotPos2)
        (solver.warehouse2.getDimensions, solver.robotPos2, solver.warehouse2.getDataPoints) shouldBe
            ((20, 10), (8, 4), SimpleGrid(w2expected).getDataPoints)
    }

    private val w2 = Vector(
        "##############",
        "##......##..##",
        "##..........##",
        "##....[][]@.##",
        "##....[]....##",
        "##..........##",
        "##############"
    )
    it should "push wide items horizontally" in {
        val grid = SimpleGrid(w2)
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (7, 3), W))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (5, 3), E))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (7, 3), W))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (9, 3), W))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (8, 3), W))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (7, 3), W))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (6, 3), W))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (5, 3), W))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (4, 3), E))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (2, 3), E))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (3, 3), E))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (4, 3), E))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (5, 3), E))
        println(grid)
    }

    it should "push wide items vertically" in {
        val grid = SimpleGrid(w2)
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (9, 3), W))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (8, 3), N))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (8, 2), N))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (8, 2), S))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (8, 3), S))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (8, 3), S))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (6, 5), N))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (6, 4), N))
        println(grid)
        println(WarehouseRobot.pushWideItems(grid, (6, 4), N))
        println(grid)
    }

    private val afterShifting3 = Vector(
        "##############",
        "##...[].##..##",
        "##...@.[]...##",
        "##....[]....##",
        "##..........##",
        "##..........##",
        "##############"
    )
    it should "move around the warehouse while pushing wide goods" in {
        val grid = SimpleGrid(w2)
        val directions = "<vv<<^^<<^^".map(Direction.fromArrow).toVector
        println(grid)
        WarehouseRobot.moveRobot(grid, grid.findFirst(ROBOT), directions, part1 = false)
        println(grid)
        grid.getDataPoints shouldBe SimpleGrid(afterShifting3).getDataPoints
    }

    private val afterShifting4 = Vector(
        "####################",
        "##[].......[].[][]##",
        "##[]...........[].##",
        "##[]........[][][]##",
        "##[]......[]....[]##",
        "##..##......[]....##",
        "##..[]............##",
        "##..@......[].[][]##",
        "##......[][]..[]..##",
        "####################"
    )
    it should "solve part2 correctly" in {
        println(solver.warehouse2)
        val result = solver.part2
        println(solver.warehouse2)
        (result, solver.warehouse2.getDataPoints) shouldBe(9021, SimpleGrid(afterShifting4).getDataPoints)
    }
}
