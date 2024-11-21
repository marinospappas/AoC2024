package org.mpdev.scala.aoc2024
package day01

import solutions.day01.ElevatorButtons

import framework.AocMain
import org.scalatest.prop.TableDrivenPropertyChecks.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.Tables.Table

class TestDay01 extends AnyFlatSpec {

    AocMain.environment = "none"

    private val part1Params = Table(
        ("input", "expected"),
        (List("(())"), 0),
        (List("()()"), 0),
        (List("((("), 3),
        (List("(()(()("), 3),
        (List("))((((("), 3),
        (List("())"), -1),
        (List("))("), -1),
        (List(")))"), -3),
        (List(")())())"), -3)
    )

    it should "solve part1 correctly" in {
        forAll (part1Params) { (input: List[String], expected: Int) =>
            val solver = ElevatorButtons(input)
            solver.solvePart1 shouldBe expected
        }
    }

    private val part2Params = Table(
        ("input", "expected"),
        (List(")"), 1),
        (List("())()()"), 3),
        (List("()())"), 5),
        (List("()())))))"), 5)
    )

    it should "solve part2 correctly" in {
        forAll(part2Params) { (input: List[String], expected: Int) =>
            val solver = ElevatorButtons(input)
            solver.solvePart2 shouldBe expected
        }
    }
}
