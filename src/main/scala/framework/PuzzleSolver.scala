package org.mpdev.scala.aoc2024
package framework

import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait PuzzleSolver {

    def part1: Any

    def part2: Any

}

object PuzzleSolver {
    val LOG: Logger = LoggerFactory.getLogger(classOf[PuzzleSolver])
}
