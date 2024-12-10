package org.mpdev.scala.aoc2024
package day10

import framework.{AocMain, InputReader}
import utils.also
import solutions.day10.TrailFinder

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay10 extends AnyFlatSpec {

    AocMain.environment = "none"

    // TODO: tidy up the unit tests
    
    private val grid1 = List(
        "...0...",
        "...1...",
        "...2...",
        "6543456",
        "7.....7",
        "8.....8",
        "9.....9"
    )
    it should "read input and setup graph" in {
        val solver = TrailFinder(grid1)
        solver.grid.printIt()
        solver.graph.printIt()
    }

    private val grid2 = List(
        "10..9..",
        "2...8..",
        "3...7..",
        "4567654",
        "...8..3",
        "...9..2",
        ".....01"
    )
    private val solver = TrailFinder(InputReader.read(10, "src/test/resources/input/input"))

    it should "solve part1 correctly" in {
        //val solver = TrailFinder(grid2)
        val result = solver.part1.also(println)
        result shouldBe 36
    }

    private val grid3 = List(
        ".....0.",
        "..4321.",
        "..5..2.",
        "..6543.",
        "..7..4.",
        "..8765.",
        "..9...."
    )
    it should "solve part2 correctly" in {
        //val solver = TrailFinder(grid3)
        val result = solver.part2.also(println)
        result shouldBe 81
    }
}