package org.mpdev.scala.aoc2024
package day25

import framework.AocMain

import solutions.day25.LockDecoder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay25 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = LockDecoder()

    it should "read input and setup locks and keys" in {
        println("Locks")
        solver.locks.foreach(println)
        solver.lockCombinations.foreach(println)
        println("Keys")
        solver.keys.foreach(println)
        solver.keyCombinations.foreach(println)
        (solver.locks.size, solver.keys.size) shouldBe (3, 2)
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 3
    }

}
