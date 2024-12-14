package org.mpdev.scala.aoc2024
package day15

import framework.{AocMain, InputReader}
import solutions.day15.Day15Solver

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay15 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = Day15Solver()

    it should "read input and setup ...." in {
        solver.inputData.foreach(println)
        solver.inputData.size shouldBe 0
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 480
    }
    
}
