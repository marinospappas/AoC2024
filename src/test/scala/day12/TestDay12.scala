package org.mpdev.scala.aoc2024
package day12

import framework.{AocMain, InputReader}
import solutions.day12.FenceCalculator
import utils.SimpleGrid

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay12 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = FenceCalculator()

    it should "read input and setup reports list" in {
        solver.plotsGrid.printIt()
        println(solver.allCoords)
        (solver.plotsGrid.getDimensions, solver.allCoords.size) shouldBe ((10, 10), 100)
    }

    private val grid1 = List(
        "AAAA",
        "BBCD",
        "BBCC",
        "EEEC"
    )
    private val grid2 = List(
        "OOOOO",
        "OXOXO",
        "OOOOO",
        "OXOXO",
        "OOOOO"
    )

    it should "identify all plots" in {
        val grid = SimpleGrid(InputReader.read(12, "src/test/resources/input/input"))
        val plots = solver.findAllPlots(grid)
        plots.foreach(println)
        plots.map(_._2.size).sum shouldBe grid.getDimensions._1 * grid.getDimensions._2
    }

    it should "calculate perimeter" in {
        val grid = SimpleGrid(grid1)
        val plots = solver.findAllPlots(grid)
        val perimeters = plots.map( plot => (plot._1, grid.getPerimeter(plot._2)))
        perimeters.foreach(println)
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 1930
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 4
    }
}
