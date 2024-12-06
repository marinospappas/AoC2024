package org.mpdev.scala.aoc2024
package day06

import framework.AocMain
import solutions.day06.MapRouteExplorer
import utils.Point
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay06 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = MapRouteExplorer()

    it should "read input and setup ..." in {
        solver.mapGrid.printIt()
        val start = solver.startPosition
        println(start)
        solver.mapGrid.getDimensions shouldBe (10, 10)
    }

    it should "solve part1 correctly" in {
        val result = solver.part1
        solver.mapGrid.printIt()
        println(solver.guardRoute)
        result shouldBe 41
    }

    it should "solve part2 correctly" in {
        solver.part1
        val result = solver.part2
        solver.mapGrid.printIt()
        println(result)
        result shouldBe 6
    }
}