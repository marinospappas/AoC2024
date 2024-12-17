package org.mpdev.scala.aoc2024
package day18

import framework.AocMain
import solutions.day18.Day18Solver

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay18 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = Day18Solver()

    it should "read input and setup ???" in {
        println(solver.inputData)
        solver.inputData.size shouldBe 0
    }

    it should "solve part1 correctly" in {
        val result = solver.part1
        println(result)
        result shouldBe ""
    }

    it should "solve part2 correctly" in {
        val result = solver.part2
        println(result)
        result shouldBe ""
    }
}
