package org.mpdev.scala.aoc2024
package day13

import framework.{AocMain, InputReader}
import solutions.day13.ClawMachinePlayer
import solutions.day13.ClawMachinePlayer.PART2_OFFSET
import utils.LinearEqSys

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay13 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = ClawMachinePlayer()

    it should "read input and setup claw machines" in {
        solver.machines.foreach(println)
        solver.machines.size shouldBe 4
    }

    it should "calculate times to press button" in {
        val timesToPress = solver.machines.map( m => LinearEqSys.solve2Long(m.buttonA, m.buttonB, m.prize) )
        println(timesToPress)
        timesToPress shouldBe List((80,40), null, (38,86), null)
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 480
    }

    it should "calculate times to press button part 2" in {
        val timesToPress = solver.machines.map( m => LinearEqSys.solve2Long(m.buttonA, m.buttonB,
            (m.prize._1 + PART2_OFFSET, m.prize._2 + PART2_OFFSET)) )
        println(timesToPress)
        timesToPress shouldBe List(null, (118679050709L,103199174542L), null, (102851800151L,107526881786L))
    }
    
}
