package org.mpdev.scala.aoc2024
package day20

import framework.AocMain
import solutions.day20.Day20Solver

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay20 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = Day20Solver()

    it should "read input and setup reports list" in {
        solver.inputData.foreach(println(_))
        solver.inputData.size shouldBe 0
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 0
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 0
    }
}
