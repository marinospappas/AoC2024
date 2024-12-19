package org.mpdev.scala.aoc2024
package day01

import solutions.day01.LocationAnalyser

import framework.AocMain
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay01 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = LocationAnalyser()

    it should "read input lists" in {
        println(solver.listA)
        println(solver.listB)
        (solver.listA.size, solver.listB.size) shouldBe (6, 6)
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 11
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 31
    }
}
