package org.mpdev.scala.aoc2024
package day02

import framework.{AocMain, InputReader}
import solutions.day02.ReportAnalyser

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay02 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = ReportAnalyser()

    it should "read input and setup parcels list" in {
        solver.reports.foreach(println(_))
        solver.reports.size shouldBe 6
    }

    it should "do sliding window" in {
        List(1,2,3,4,5,6).sliding(2).foreach(println(_))
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 2
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 4
    }
}
