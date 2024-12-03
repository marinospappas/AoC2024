package org.mpdev.scala.aoc2024
package day03

import framework.AocMain
import solutions.day03.CorruptedMemoryCleaner
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay03 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = CorruptedMemoryCleaner()

    it should "read input and setup program memory" in {
        println(solver.inputData)
        solver.inputData.length shouldBe 149
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 161
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 136
    }
}
