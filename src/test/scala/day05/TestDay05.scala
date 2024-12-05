package org.mpdev.scala.aoc2024
package day05

import framework.AocMain
import solutions.day05.{PrintingRules, applyRule}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay05 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = PrintingRules()

    it should "read input and setup rules and pages lists" in {
        solver.orderingRules.foreach(println)
        solver.manSections.foreach(println)
        (solver.orderingRules.size, solver.manSections.size) shouldBe (21, 6)
    }

    it should "apply printing rules" in {
        val correctlyOrdered = solver.manSections.filter (section =>
            solver.orderingRules.forall (rule => section.applyRule(rule))
        )
        println(correctlyOrdered)
        correctlyOrdered shouldBe List(
            List(75,47,61,53,29), List(97,61,53,29,13), List(75,29,13)
        )
    }

    it should "solve part1 correctly" in {
        val result = solver.part1
        println(result)
        result shouldBe 143
    }

    it should "solve part2 correctly" in {
        val result = solver.part2
        println(result)
        result shouldBe 123
    }
}