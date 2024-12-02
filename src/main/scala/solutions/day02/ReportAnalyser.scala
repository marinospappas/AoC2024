package org.mpdev.scala.aoc2024
package solutions.day02

import framework.{InputReader, PuzzleSolver}

import scala.collection.mutable.ArrayBuffer
import scala.math.abs
import scala.util.boundary
import scala.util.boundary.break

class ReportAnalyser extends PuzzleSolver {

    val reports: List[List[Int]] = InputReader.read(2).map( s => s.transform() )

    override def part1: Any =
        reports.count( r => r.isSafe )

    override def part2: Any =
        var safeCount = 0
        for (r <- reports) {
            if (r.isSafe)
                safeCount += 1
            else {
                boundary:
                    for (index <- r.indices) {
                        val a = r.to(ArrayBuffer)
                        if ({ a.remove(index)
                            a.toList.isSafe }) {
                            safeCount += 1
                            break()
                        }
                    }
            }
        }
        safeCount

    extension (l: List[Int])
        def isSafe: Boolean = {
            val lSorted = l.sorted
            if (l != lSorted && l != lSorted.reverse)
                return false
            var safe = true
            boundary:
                for (i <- 0 to l.size - 2) do
                    if (abs(l(i) - l(i+1)) > 3 || l(i) == l(i+1)) {
                        safe = false
                        break()
                    }
            safe
        }

    extension (s: String)
        private def transform(): List[Int] = s.split(""" +""").map(a => a.toInt).toList
}
