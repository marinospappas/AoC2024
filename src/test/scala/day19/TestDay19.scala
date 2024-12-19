package org.mpdev.scala.aoc2024
package day19

import framework.AocMain
import solutions.day19.StripePatterns

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.TableDrivenPropertyChecks.forAll
import org.scalatest.prop.Tables.Table

class TestDay19 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = StripePatterns()

    it should "read input and setup reports list" in {
        println(solver.availablePatterns.mkString("[", ",", "]"))
        solver.givenCombinations.foreach(println(_))
        (solver.availablePatterns.length, solver.givenCombinations.size) shouldBe (8, 8)
    }

    private val params = Table(
        ("pattern", "expected"),
        ("brwrr", 2),
        ("bggr", 1),
        ("gbbr", 4),
        ("rrbgbr", 6),
        ("ubwu", 0),
        ("bwurrg", 1),
        ("brgr", 2),
        ("bbrgwb", 0),
    )
    it should "find count pattern combinations" in {
        forAll(params) { (ptrn: String, expected: Int) =>
            val result = StripePatterns.getCountOfPatternCombinations(ptrn, solver.availablePatterns)
            println(result)
            result shouldBe expected
        }
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 6
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 16
    }
}
