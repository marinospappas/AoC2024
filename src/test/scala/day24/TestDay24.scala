package org.mpdev.scala.aoc2024
package day24

import framework.AocMain
import solutions.day24.LogicalCircuit

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay24 extends AnyFlatSpec {

    AocMain.environment = "prod"
    private val solver = LogicalCircuit()

    it should "read input and setup logical circuit" in {
        solver.inputSignals.foreach(println)
        solver.gates.values.foreach(println)
        solver.outputs.foreach(println)
        (solver.inputSignals.size, solver.gates.size, solver.outputs.size) shouldBe (10, 36, 13)
    }

    it should "calculate circuit output" in {
        val testData = Vector(
            "x00: 1",
            "x01: 1",
            "x02: 1",
            "y00: 0",
            "y01: 1",
            "y02: 0",
            "",
            "x00 AND y00 -> z00",
            "x01 XOR y01 -> z01",
            "x02 OR y02 -> z02"
        )
        val solver = LogicalCircuit(testData)
        val z00 = solver.calculateCircuitOutput("z00", solver.inputSignals)
        val z01 = solver.calculateCircuitOutput("z01", solver.inputSignals)
        val z02 = solver.calculateCircuitOutput("z02", solver.inputSignals)
        println(s"$z02$z01$z00")
        (z02, z01, z00) shouldBe (1, 0, 0)
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 2024
    }

    it should "print diagram of circuit" in {
        solver.printCircuit()
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 0
    }
}
