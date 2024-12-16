package org.mpdev.scala.aoc2024
package day16

import framework.{AocMain, InputReader}
import utils.SimpleGrid
import utils.SimpleGrid.Direction
import utils.SimpleGrid.Direction.*
import solutions.day16.ReindeerMinRoute
import solutions.day16.ReindeerMinRoute.backTrack

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay16 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = ReindeerMinRoute()

    it should "read input and setup route map" in {
        println(solver.grid)
        println(solver.startState)
        (solver.grid.getDimensions, solver.startState._1) shouldBe ((17, 17), (1, 15))
    }

    it should "solve part1 correctly" in {
        val result = solver.part1
        println(solver.allPaths(0))
        result shouldBe 11048
    }

    private val map1 = Vector(
        "#######",
        "#####E#",
        "#####.#",
        "#.....#",
        "#.###.#",
        "#.###.#",
        "#.....#",
        "#.#####",
        "#S#####",
        "#######"
    )
    it should "find multiple min paths" in {
        val solver = ReindeerMinRoute(map1)
        solver.part1
        println(solver.grid)
        solver.allPaths.foreach(println)
        solver.allPaths.map(_.map(_._1).sliding(2).toVector.flatMap(v => backTrack(v.head, v.last)).distinct)
            .foreach(println)
        val pts = solver.allPaths
            .flatMap(_.map(_._1).sliding(2).toVector.flatMap(v => backTrack(v.head, v.last)).distinct)
            .distinct
        println(pts)
        pts.size shouldBe 18
    }

    it should "solve part2 correctly" in {
        solver.part1
        val result = solver.part2
        println(result)
        result shouldBe 64
    }
}
