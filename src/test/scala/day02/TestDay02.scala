package org.mpdev.scala.aoc2024
package day02

import framework.{AocMain, InputReader}
import solutions.day02.Day02Solver

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay02 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = Day02Solver()

    it should "read input and setup parcels list" in {
        solver.inputData.foreach(println(_))    
        solver.inputData.size shouldBe 2
    }
    
    it should "solve part1 correctly" in {
        solver.part1 shouldBe 101
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 48
    }
}
