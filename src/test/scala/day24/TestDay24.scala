package org.mpdev.scala.aoc2024
package day24

import framework.AocMain
import solutions.day24.LogicalCircuit

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay24 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = LogicalCircuit()

    it should "read input and setup logical circuit" in {
        solver.inputSignals.foreach(println(_))
        solver.inputSignals.size shouldBe 10
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 0
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 0
    }
}
