package org.mpdev.scala.aoc2024
package day08

import framework.AocMain
import solutions.day08.AntennaPositionAnalyser

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay08 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = AntennaPositionAnalyser()

    it should "read input and setup grid of Antennas" in {
        solver.grid.printIt()
        solver.antennas.foreach(println)
        solver.grid.getDimensions shouldBe (12, 12)
    }

    it should "find first anti-nodes for pair" in {
        val result = solver.findFirstAntinodes(List((5,2), (8,1)))
        result.filter(solver.grid.getDataPoint(_) == AntennaPositionAnalyser.EMPTY)
            .foreach(solver.grid.setDataPoint(_, AntennaPositionAnalyser.ANTI_NODE))
        solver.grid.printIt()
        println(result)
        result shouldBe Set((11, 0), (2, 3))
    }

    it should "find all anti-nodes for pair" in {
        val result = solver.findAllAntinodes(List((5, 2), (7, 3)))
        result.filter(solver.grid.getDataPoint(_) == AntennaPositionAnalyser.EMPTY)
            .foreach(solver.grid.setDataPoint(_, AntennaPositionAnalyser.ANTI_NODE))
        solver.grid.printIt()
        println(result)
        result shouldBe Set((1, 0), (3, 1), (5, 2), (7, 3), (9, 4), (11, 5))
    }

    it should "solve part1 correctly" in {
        val result = solver.findAntinodes(part1 = true)
        result.filter(solver.grid.getDataPoint(_) == AntennaPositionAnalyser.EMPTY)
            .foreach(solver.grid.setDataPoint(_, AntennaPositionAnalyser.ANTI_NODE))
        solver.grid.printIt()
        println(result)
        result.size shouldBe 14
    }

    it should "solve part2 correctly" in {
        val result = solver.findAntinodes(part1 = false)
        result.filter(solver.grid.getDataPoint(_) == AntennaPositionAnalyser.EMPTY)
            .foreach(solver.grid.setDataPoint(_, AntennaPositionAnalyser.ANTI_NODE))
        solver.grid.printIt()
        println(result)
        result.size shouldBe 34
    }
}