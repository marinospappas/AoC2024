package org.mpdev.scala.aoc2024
package day11

import framework.{AocMain, InputReader}
import solutions.day10.TrailFinder
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
        val result = solver.transformStonesSeq(List("0", "1", "10", "99", "999").map(Stone.fromString(0, _)))
        println(result)
        result shouldBe List("1", "2024", "1", "0", "9", "9", "2021976").map(Stone.fromString(1, _))
    }

    it should "traverse stones list using bfs" in {
        val result = solver.traverseStonesBfs(Stone.START, 25)
        println(result)
        //result shouldBe List("1", "2024", "1", "0", "9", "9", "2021976").map(Stone.fromString(1, _))
    }


    it should "solve part1 correctly" in {
        val result = solver.part1.also(println)
        result shouldBe 55312
    }
}