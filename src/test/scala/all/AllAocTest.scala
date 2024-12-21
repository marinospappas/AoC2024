package org.mpdev.scala.aoc2024
package all

import framework.{AocMain, PuzzleSolver, results, solvers}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.Inspectors.forEvery
import org.scalatest.matchers.should.Matchers.*

class AllAocTest extends AnyFlatSpec {

    AocMain.environment = "prod"

    it should "solve all AoC puzzles" in {
        forEvery( for i <- 1 to 25 yield (solvers(i), i, results(i)) ) { 
            (solver: PuzzleSolver, day: Int, expected: (String, String)) =>
                val (res1, res2) = (solver.part1, solver.part2)
                println(s"day $day: $res1, $res2")
                (res1.toString, res2.toString) shouldBe expected
        }
    }
}
