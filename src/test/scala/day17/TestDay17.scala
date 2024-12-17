package org.mpdev.scala.aoc2024
package day17

import framework.AocMain
import solutions.day17.ByteComputer17

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay17 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = ByteComputer17()

    it should "read input and setup program" in {
        solver.regInit.foreach(println)
        println(solver.progList)
        (solver.regInit.size, solver.progList.size) shouldBe (3, 6)
    }

    private val prog1 = Vector(
        "Register C: 9",
        "",
        "Program: 2,6"
    )
    it should "run simple program" in {
        val solver = ByteComputer17(prog1)
        solver.aocVm.newProgram
        solver.aocVm.runProgram(solver.regInit)
        solver.aocVm.waitForProgram()
        solver.aocVm.getProgramRegister("B") shouldBe 1
    }

    private val prog2 = Vector(
        "Register A: 10",
        "",
        "Program: 5,0,5,1,5,4"
    )
    it should "run simple program-2" in {
        val solver = ByteComputer17(prog2)
        val result = solver.part1
        result shouldBe "0,1,2"
    }

    private val prog3 = Vector(
        "Register A: 2024",
        "",
        "Program: 0,1,5,4,3,0"
    )
    it should "run simple program-3" in {
        val solver = ByteComputer17(prog3)
        val result = solver.part1
        println(result)
        solver.aocVm.getProgramRegister("A") shouldBe 0
    }

    it should "solve part1 correctly" in {
        val solver = ByteComputer17()
        solver.part1 shouldBe 12
    }
        
}
