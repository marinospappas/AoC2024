package org.mpdev.scala.aoc2024
package day23

import framework.AocMain
import solutions.day23.InterconnectedComputers

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay23 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = InterconnectedComputers()

    it should "read input and setup grid of connected computers" in {
        solver.inputData.foreach(println(_))
        solver.connections.foreach(println)
        (solver.inputData.size, solver.connections.size, solver.allIds.size) shouldBe (32, 32, 16)
    }

    it should "identify groups of 3 connected computers" in {
        solver.findConnectedSetsN(3)
        solver.connectionsN.foreach(println)
        (solver.connectionsN.size, solver.connectionsN.forall( _.size == 3 )) shouldBe (12, true)
    }

    it should "solve part1 correctly" in {
        (solver.part1, solver.connectionsN.size) shouldBe (7, 12)
    }

    it should "solve part2 correctly" in {
        solver.part1
        solver.part2 shouldBe "co,de,ka,ta"
    }
}
