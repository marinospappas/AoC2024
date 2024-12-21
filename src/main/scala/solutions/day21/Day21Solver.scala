package org.mpdev.scala.aoc2024
package solutions.day21

import framework.{InputReader, PuzzleSolver}

class Day21Solver extends PuzzleSolver {

    val inputData: Vector[String] = InputReader.read(0)

    override def part1: Any =
        0

    override def part2: Any =
        0
}

object Day21Solver {

    val numKeyPad = List(
        "+---+---+---+\n| 7 | 8 | 9 |\n+---+---+---+\n| 4 | 5 | 6 |\n+---+---+---+\n| 1 | 2 | 3 |\n+---+---+---+\n    | 0 | A |\n    +---+---+"
    )
    val dirKeypad = List(
        "    +---+---+\n    | ^ | A |\n+---+---+---+\n| < | v | > |\n+---+---+---+"
    )
}
