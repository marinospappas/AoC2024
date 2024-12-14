package org.mpdev.scala.aoc2024
package solutions.day13

import framework.{InputReader, PuzzleSolver}
import solutions.day13.ClawMachinePlayer.{PART2_OFFSET, TOKENS_A, TOKENS_B, toClawMachine}
import utils.LinearEqSys

class ClawMachinePlayer extends PuzzleSolver {

    val machines: List[ClawMachine] = InputReader.read(13).sliding(4, 4).toList.map( toClawMachine )

    override def part1: Any =
        machines.map( m => LinearEqSys.solve2Long(m.buttonA, m.buttonB, m.prize) ).filter( _ != null )
            .foldLeft(0L) ( (res, cur) => res + TOKENS_A * cur._1 + cur._2 * TOKENS_B )

    override def part2: Any =
        machines.map( m => LinearEqSys.solve2Long(m.buttonA, m.buttonB,
            (m.prize._1 + PART2_OFFSET, m.prize._2 + PART2_OFFSET))
        ).filter( _ != null ).foldLeft(0L) ( (res, cur) => res + TOKENS_A * cur._1 + cur._2 * TOKENS_B )
}

object ClawMachinePlayer {

    val TOKENS_A = 3
    val TOKENS_B = 1
    val PART2_OFFSET = 10000000000000L

    // input parsing
    private def readButton(s: String): (Int, Int) =
        val matched = """Button [AB]: X\+(\d+), Y\+(\d+)""".r.findFirstMatchIn(s).get
        (matched.group(1).toInt, matched.group(2).toInt)

    private def readPrize(s: String): (Int, Int) =
        s match { case s"Prize: X=${p1}, Y=${p2}" => (p1.toInt, p2.toInt) }

    def toClawMachine(l: List[String]): ClawMachine =
        ClawMachine(readButton(l.head), readButton(l(1)), readPrize(l(2)))
}
