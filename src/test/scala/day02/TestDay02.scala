package org.mpdev.scala.aoc2024
package day02

import framework.{AocMain, InputReader}
import solutions.day02.PresentWrapping

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay02 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = PresentWrapping()

    it should "solve part1 correctly" in {
        solver.solvePart1 shouldBe 101
    }

    it should "solve part2 correctly" in {
        solver.solvePart2 shouldBe 48
    }
}
