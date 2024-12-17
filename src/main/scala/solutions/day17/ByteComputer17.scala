package org.mpdev.scala.aoc2024
package solutions.day17

import framework.{InputReader, PuzzleSolver}
import solutions.day13.ClawMachinePlayer.{PART2_OFFSET, TOKENS_A, TOKENS_B, toClawMachine}
import utils.LinearEqSys
import solutions.day17.ByteComputer17.{readRegister, toProgram}
import utils.aocvm.AocVm
import utils.aocvm.bytecode.ByteProgram

class ByteComputer17(var testData: Vector[String] = Vector()) extends PuzzleSolver {

    val inputData: Vector[String] = if (testData.nonEmpty) testData else InputReader.read(17)
    val regInit: Map[String, Long] = inputData.slice(0, inputData.indexOf("")).map(readRegister).toMap
    val progList: Vector[String] = toProgram(inputData.last)

    val aocVm = AocVm(progList, ByteProgram())

    override def part1: Any = {
        aocVm.newProgram
        aocVm.runProgram(regInit)
        //while aocVm.programIsRunning() do {
        //    println(aocVm.getAsyncOutputFromProgram())
        //}
        aocVm.waitForProgram()
        aocVm.getFinalOutputFromProgram.mkString(",")
    }

    override def part2: Any =
        ""
}

object ByteComputer17 {

    // input parsing
    private def readRegister(s: String): (String, Long) =
        s match { case s"Register ${r}: ${v}" => (r, v.toLong) }

    def toProgram(s: String): Vector[String] =
        s match { case s"Program: ${p}" => p.split(",").toVector }
}
