package org.mpdev.scala.aoc2024
package day05

import framework.AocMain
import solutions.day05.Day05Solver
import utils.{GridUtils, Point}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.TableDrivenPropertyChecks.forAll
import org.scalatest.prop.Tables.Table

class TestDay05 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = Day05Solver()

    it should "read input and setup input words grid" in {
        solver.inputData.foreach(println)
        solver.inputData.size shouldBe ???
    }

    private val wordMatchParams = Table(
        ("point", "expected"),
        (Point(6,4), true),
    )

    it should "find word matches" in {
        forAll(wordMatchParams) { (p: Point, expected: Boolean) =>
            solver.part1 shouldBe expected
        }
    }

    it should "solve part1 correctly" in {
        println(solver.part1)
        solver.part1 shouldBe ""
    }

    it should "solve part2 correctly" in {
        println(solver.part2)
        solver.part2 shouldBe ""
    }
}
