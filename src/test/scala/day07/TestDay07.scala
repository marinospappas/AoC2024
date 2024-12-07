package org.mpdev.scala.aoc2024
package day07

import framework.AocMain
import solutions.day07.BridgeRepair
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay07 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = BridgeRepair()

    it should "read input and setup list" in {
        solver.inputData.foreach((n, l) => println(s"$n: ${l.mkString(",")}"))
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