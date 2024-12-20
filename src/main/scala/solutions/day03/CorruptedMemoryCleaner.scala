package org.mpdev.scala.aoc2024
package solutions.day03

import framework.{InputReader, PuzzleSolver}

import scala.util.matching.Regex

class CorruptedMemoryCleaner extends PuzzleSolver {

    val inputData: String = InputReader.read(3).mkString
    private val mul: Regex = """mul\((\d{1,3}),(\d{1,3})\)""".r
    private val doMul: Regex = """do\(\)""".r
    private val dontMul: Regex = """don't\(\)""".r

    override def part1: Any =
        mul.findAllMatchIn(inputData).map( m => m.group(1).toInt * m.group(2).toInt ).sum

    override def part2: Any =
        s"${mul.regex}|${doMul.regex}|${dontMul.regex}".r.findAllMatchIn(inputData)
            .foldLeft((true, 0)) ({ case ((doIt, sum), m) =>
                (doIt, m.matched) match
                    case (_, "do()") => (true, sum)
                    case (_, "don't()") => (false, sum)
                    case (true, _) => (true, sum + m.group(1).toInt * m.group(2).toInt)
                    case _ => (doIt, sum)
            })._2
}
