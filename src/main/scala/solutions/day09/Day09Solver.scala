package org.mpdev.scala.aoc2024
package solutions.day09

import framework.{InputReader, PuzzleSolver}
import utils.Graph

class Day09Solver extends PuzzleSolver {
    
    val inputData: List[String] = InputReader.read(9)
    
    override def part1: Any =
        ""
        
    override def part2: Any =
        ""

    // input parsing
    private def readRule(s: String): (Int, Int) =
        s match { case s"${p1}|${p2}" => (p1.toInt, p2.toInt) }

    private def readPagesSection(s: String): List[Int] =
        s.split(",").map(a => a.toInt).toList
}
