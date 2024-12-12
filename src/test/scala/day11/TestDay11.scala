package org.mpdev.scala.aoc2024
package day11

import framework.{AocMain, InputReader}
import utils.also
import solutions.day11.PebbleTransformation

import solutions.day11.PebbleTransformation.Stone
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay11 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = PebbleTransformation()

    it should "read input and setup stones series" in {
        solver.inputData.foreach(println)
        solver.inputData.size shouldBe 2
    }

    it should "executes transformation rules" in {
        val result = solver.transformWithDictionary(List("0", "1", "10", "99", "999").map(Stone.fromString), 1)
        result.foreach(println)
        val expectedResult = List("1", "2024", "1", "0", "9", "9", "2021976") //.map(Stone.fromString -> 1)
        result.toList shouldBe expectedResult
    }

    it should "solve part1 correctly" in {
        val result = solver.part1.also(println)
        result shouldBe 55312
    }

    it should "solve part2 correctly" in {
        val result = solver.part2.also(println)
        result shouldBe 65601038650482L
    }

}