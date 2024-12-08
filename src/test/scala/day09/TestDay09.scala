package org.mpdev.scala.aoc2024
package day09

import framework.AocMain
import solutions.day09.Day09Solver
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay09 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = Day09Solver()

    it should "read input and setup list" in {
        solver.inputData.foreach(println)
        solver.inputData.size shouldBe 9
    }

    it should "solve part1 correctly" in {
        val result = solver.part1
        println(result)
        result shouldBe 3749
    }

    it should "solve part2 correctly" in {
        val result = solver.part2
        println(result)
        result shouldBe 11387
    }
}