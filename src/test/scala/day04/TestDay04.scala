package org.mpdev.scala.aoc2024
package day04

import framework.AocMain
import solutions.day04.WordSearch
import utils.{GridUtils, Point}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.TableDrivenPropertyChecks.forAll
import org.scalatest.prop.Tables.Table

class TestDay04 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = WordSearch()

    it should "read input and setup input words grid" in {
        solver.grid.printIt()
        solver.grid.getDimensions shouldBe (10, 10)
    }

    private val wordMatchParams = Table(
        ("point", "direction", "expected"),
        (Point(6,4), GridUtils.Direction.UP, true),
        (Point(6,4), GridUtils.Direction.LEFT, true),
        (Point(6,4), GridUtils.Direction.RIGHT, false),
        (Point(1,4), GridUtils.Direction.RIGHT, false),
    )

    it should "find word matches" in {
        forAll(wordMatchParams) { (p: Point, dir: GridUtils.Direction, expected: Boolean) =>
            solver.matchesWord(p, dir) shouldBe expected
        }
    }

    private val wordCountParams = Table(
        ("point", "expected"),
        (Point(6, 4), 2),
        (Point(4, 2), 0),
        (Point(1, 5), 0),
    )

    it should "find word count" in {
        forAll(wordCountParams) { (p: Point, expected: Int) =>
            solver.countWordMatches(p) shouldBe expected
        }
    }

    it should "solve part1 correctly" in {
        println(solver.part1)
        solver.part1 shouldBe 18
    }

    it should "solve part2 correctly" in {
        println(solver.part2)
        solver.part2 shouldBe 9
    }
}
