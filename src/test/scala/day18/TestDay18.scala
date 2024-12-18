package org.mpdev.scala.aoc2024
package day18

import framework.AocMain
import solutions.day18.MemoryMaze
import solutions.day18.MemoryState.CORRUPTED
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay18 extends AnyFlatSpec {

    AocMain.environment = "test"
    {
        MemoryMaze.NUM_OF_CORRUPTED = 12
        MemoryMaze.GRID_DIMENSIONS = (7, 7)
    }
    private val solver = MemoryMaze()

    it should "read input and setup grid" in {
        println(solver.grid)
        (solver.grid.getDimensions, solver.grid.countOf(CORRUPTED)) shouldBe ((7, 7), 12)
    }

    it should "solve part1 correctly" in {
        val result = solver.part1
        println(solver.grid)
        println(result)
        result shouldBe 22
    }

    it should "solve part2 correctly" in {
        val result = solver.part2
        println(result)
        result shouldBe "6,1"
    }
}
