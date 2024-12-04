package org.mpdev.scala.aoc2024
package day04

import framework.AocMain
import solutions.day04.WordSearch
import utils.{GridUtils, Point}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay04 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = WordSearch()

    it should "read input and setup input words grid" in {
        solver.grid.printIt()
        solver.grid.getDimensions shouldBe (10, 10)
    }

    it should "find word matches" in {
        println(solver.matchesWord(Point(6,4), GridUtils.Direction.UP))
        println(solver.matchesWord(Point(6,4), GridUtils.Direction.LEFT))
        println(solver.matchesWord(Point(6,4), GridUtils.Direction.RIGHT))
        println(solver.countWordMatches(Point(6,4)))
    }

    it should "solve part1 correctly" in {
        println(solver.part1)
        solver.part1 shouldBe 18
    }

    it should "solve part2 correctly" in {
        println(solver.part2)
        solver.part2 shouldBe 9
    }
}
