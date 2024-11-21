package org.mpdev.scala.aoc2024
package framework

import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait PuzzleSolver {

    val log: Logger = LoggerFactory.getLogger(classOf[PuzzleSolver])

    def part1: (Any, Long) = {
        val start = System.currentTimeMillis()
        (solvePart1, System.currentTimeMillis() - start)
    }

    def part2: (Any, Long) = {
        val start = System.currentTimeMillis()
        (solvePart2, System.currentTimeMillis() - start)
    }

    def solvePart1: Any

    def solvePart2: Any

}
