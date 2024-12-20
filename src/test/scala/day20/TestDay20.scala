package org.mpdev.scala.aoc2024
package day20

import framework.AocMain
import solutions.day20.RaceCondition

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay20 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = RaceCondition()

    it should "read input and setup map" in {
        println(solver.grid)
        println(solver.start)
        println(solver.graph)
        println(solver.route)
        (solver.grid.getDimensions, solver.start, solver.route.length) shouldBe((15, 15), (1, 3), 85)
    }

    it should "solve part1 correctly" in {
        RaceCondition.SHORTCUT_CRITERIA = 10
        val result = solver.part1
        println(result)
        result shouldBe 10
    }

    it should "solve part2 correctly" in {
        RaceCondition.SHORTCUT_CRITERIA = 50
        val result = solver.part2
        println(result)
        result shouldBe 285
    }
}
