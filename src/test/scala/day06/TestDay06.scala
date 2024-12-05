package org.mpdev.scala.aoc2024
package day06

import framework.AocMain
import solutions.day06.Day06Solver
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay06 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = Day06Solver()

    it should "read input and setup ..." in {
        solver.inputData.foreach(println)
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