package org.mpdev.scala.aoc2024
package day11

import framework.{AocMain, InputReader}
import solutions.day10.TrailFinder
import utils.also

import solutions.day11.Day11Solver
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay11 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = Day11Solver()
    
    it should "read input and setup ....." in {
        solver.inputData.foreach(println)
        solver.inputData.size shouldBe 11111
    }
    
    it should "solve part1 correctly" in {
        val result = solver.part1.also(println)
        result shouldBe 36
    }
    
    it should "solve part2 correctly" in {
        //val solver = TrailFinder(grid3)
        val result = solver.part2.also(println)
        result shouldBe 81
    }
}