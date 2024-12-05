package org.mpdev.scala.aoc2024
package solutions.day06

import framework.{InputReader, PuzzleSolver}

import scala.collection.mutable.ArrayBuffer
import scala.math.abs
import scala.util.boundary
import scala.util.boundary.break

class Day06Solver extends PuzzleSolver {

    val inputData: List[String] = InputReader.read(6)

    override def part1: Any =
        ""

    override def part2: Any =
        ""

    // input parsing
    private def readRule(s: String): (Int, Int) =
        s match {
            case s"${p1}|${p2}" => (p1.toInt, p2.toInt)
        }

    private def readPagesSection(s: String): List[Int] =
        s.split(",").map(a => a.toInt).toList
}
