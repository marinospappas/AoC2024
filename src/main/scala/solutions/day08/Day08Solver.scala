package org.mpdev.scala.aoc2024
package solutions.day08

import framework.{InputReader, PuzzleSolver}

import scala.collection.mutable.ArrayBuffer

class Day08Solver extends PuzzleSolver {

    val inputData: List[String] = InputReader.read(8)

    override def part1: Any =
        ""

    override def part2: Any =
        ""

    // input parsing
    private def parse(s: String): (Long, List[Long]) =
        s match { case s"${p1}: ${p2}" => p1.toLong -> p2.split(" ").map(_.toLong).toList }
}