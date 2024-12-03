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
        reports.filter(r => !r.isSafe).map( r => {
            boundary:
                for index <- r.indices do
                    if { val a = r.to(ArrayBuffer)
                        a.remove(index)
                        a.toList.isSafe
                    } then break(1)
                0
        }).sum + reports.count( r => r.isSafe )

    extension (l: List[Int])
        private def isSafe: Boolean = {
            val lSorted = l.sorted
            val sorted = l == lSorted
            val reverseSorted = l == lSorted.reverse
            (sorted || reverseSorted) && l.sliding(2).forall( a => a.head != a(1) && abs(a.head - a(1)) <= 3 )
        }

    extension (s: String)
        private def transform(): List[Int] = s.split(""" +""").map(a => a.toInt).toList
}
