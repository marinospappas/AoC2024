package org.mpdev.scala.aoc2024
package day23

import framework.AocMain
import solutions.day23.InterconnectedComputers

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay23 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = InterconnectedComputers()

    it should "read input and setup grid of computers" in {
        solver.inputData.foreach(println(_))
        println(solver.graph)
        solver.inputData.size shouldBe 32
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 0
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 0
    }
}
