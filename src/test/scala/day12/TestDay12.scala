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
        (solver.plotsGrid.getDimensions, solver.plotsGrid.getAllCoordinates.size) shouldBe ((10, 10), 100)
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
        val grid = SimpleGrid(grid1)
        val plots = solver.findAllPlots(grid)
        plots.foreach( plot => println(plot._2.sortWith( (p1, p2) => SimpleGrid.compareYX(p1, p2))))
        (plots.size, plots.map(_._2.size).sum) shouldBe (5, grid.getDimensions._1 * grid.getDimensions._2)
    }

    it should "calculate perimeter" in {
        val grid = SimpleGrid(grid2)
        val plots = solver.findAllPlots(grid)
        val perimeters = plots.map( plot => (plot._1, grid.getPerimeter(plot._2)))
        perimeters.foreach(println)
        perimeters.map(_._2).sum shouldBe 52
    }
    
    private val grid3 = List(
        "EEEEE",
        "EXXXX",
        "EEEEE",
        "EXXXX",
        "EEEEE"
    )
    private val grid4 = List(
        "AAAAAA",
        "AAA..A",
        "AAA..A",
        "A..AAA",
        "A..AAA",
        "AAAAAA"
    )
    it should "calculate sides" in {
        val grid = SimpleGrid(grid4)
        val plots = solver.findAllPlots(grid)
        val sides = plots.map(plot => (plot._1, grid.getNumberOfSides(plot._2)))
        sides.foreach(println)
        sides.find( _._1 == 'A').get._2 shouldBe 12
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 1930
    }

    it should "solve part2 correctly" in {
        solver.part1
        solver.part2 shouldBe 1206
    }
}
