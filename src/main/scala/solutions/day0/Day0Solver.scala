package org.mpdev.scala.aoc2024
package solutions.day0

import framework.{InputReader, PuzzleSolver}
import solutions.day13.ClawMachinePlayer.{PART2_OFFSET, TOKENS_A, TOKENS_B, toClawMachine}
import utils.LinearEqSys

class Day0Solver extends PuzzleSolver {

    val inputData: Vector[String] = InputReader.read(0)

    override def part1: Any =
        0

    override def part2: Any =
        0
}

object Day0Solver {

    // input parsing
    private def readButton(s: String): (Int, Int) =
        val matched = """Button [AB]: X\+(\d+), Y\+(\d+)""".r.findFirstMatchIn(s).get
        (matched.group(1).toInt, matched.group(2).toInt)

    private def readPrize(s: String): (Int, Int) =
        s match { case s"Prize: X=${p1}, Y=${p2}" => (p1.toInt, p2.toInt) }

}
