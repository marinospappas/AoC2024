package org.mpdev.scala.aoc2024
package all

import framework.{PuzzleSolver, results, solvers}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.TableDrivenPropertyChecks.forAll
import org.scalatest.prop.Tables.Table

class AllAocTest extends AnyFlatSpec {

    private val params = Table(
        ("solver", "day", "expected"),
        (solvers(1), 1, results(1)),
        (solvers(2), 2, results(2)),
        (solvers(3), 3, results(3)),
        (solvers(4), 4, results(4)),
        (solvers(5), 5, results(5)),
        (solvers(6), 6, results(6)),
        (solvers(7), 7, results(7)),
        (solvers(8), 8, results(8)),
        (solvers(9), 9, results(9)),
        (solvers(10), 10, results(10)),
        (solvers(11), 11, results(11)),
        (solvers(12), 12, results(12)),
        (solvers(13), 13, results(13)),
        (solvers(14), 14, results(14)),
        (solvers(15), 15, results(15)),
        (solvers(16), 16, results(16)),
        (solvers(17), 17, results(17)),
        (solvers(18), 18, results(18)),
        (solvers(19), 19, results(19)),
        (solvers(20), 20, results(20))
    )

    it should "solve all AoC puzzles" in {
        forAll(params) { (solver: PuzzleSolver, day: Int, expected: (String, String)) =>
            val (res1, res2) = (solver.part1, solver.part2)
            println(s"day $day: $res1, $res2")
            (res1.toString, res2.toString) shouldBe expected
        }
    }

}
