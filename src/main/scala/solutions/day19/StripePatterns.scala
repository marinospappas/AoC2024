package org.mpdev.scala.aoc2024
package solutions.day19

import framework.{InputReader, PuzzleSolver}
import solutions.day19.StripePatterns.getCountOfPatternCombinations

import scala.collection.mutable

class StripePatterns extends PuzzleSolver {

    val inputData: Vector[String] = InputReader.read(19)
    val availablePatterns: Array[String] = inputData.head.split(", ")
    val givenCombinations: Vector[String] = inputData.slice(2, inputData.size)

    override def part1: Any =
        givenCombinations.map(p => getCountOfPatternCombinations(p, availablePatterns)).count(_ != 0)

    override def part2: Any =
        givenCombinations.map(p => getCountOfPatternCombinations(p, availablePatterns)).sum
}

object StripePatterns {

    private val cache: mutable.Map[String, Long] = mutable.Map[String, Long]()

    def getCountOfPatternCombinations(pattern: String, availablePatterns: Array[String]): Long = {
        if cache.contains(pattern) then
            return cache(pattern)
        var count = 0L
        val matchedPtrns = availablePatterns.filter(p => pattern.endsWith(p))
        matchedPtrns.foreach(ptrn =>
            val prevPtrn = pattern.substring(0, pattern.length - ptrn.length)
            count += (if prevPtrn.isEmpty then 1L
            else getCountOfPatternCombinations(prevPtrn, availablePatterns))
        )
        cache(pattern) = count
        count
    }
}
