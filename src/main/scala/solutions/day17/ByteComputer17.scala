package org.mpdev.scala.aoc2024
package solutions.day17

import framework.{InputReader, PuzzleSolver}
import solutions.day17.ByteComputer17.{processOctalDigits, readRegister, toProgram}
import utils.aocvm.AocVm
import utils.aocvm.bytecode.ByteProgram
import utils.aocvm.bytecode.InstructionSet.opCodeFromByte

import scala.collection.mutable.ArrayBuffer
import scala.math.pow


class ByteComputer17(var testData: Vector[String] = Vector()) extends PuzzleSolver {

    val inputData: Vector[String] = if (testData.nonEmpty) testData else InputReader.read(17)
    val regInit: Map[String, Long] = inputData.slice(0, inputData.indexOf("")).map(readRegister).toMap
    val progList: Vector[String] = toProgram(inputData.last)

    val aocVm = AocVm(progList, ByteProgram())
    {
        aocVm.newProgram    // create instance of our program in the VM
    }

    override def part1: Any = {
        try {
            aocVm.runProgramAndWait(regInit, timeout = 50)
        } catch
            case e: Exception => println(s"exception occurred: ${e.getMessage}")
        aocVm.getFinalOutputFromProgram.mkString(",")
    }

    override def part2: Any = {
        val alpha: ArrayBuffer[Long] = ArrayBuffer(0L)
        for out <- progList.reverse do {
            val aCopy = ArrayBuffer(alpha).flatten
            alpha.clear()
            aCopy.foreach ( a => (0L to 7L).foreach( i =>
                if processOctalDigits(8L * a + i) == out.toInt then
                    alpha += 8L * a + i
            ) )
        }
        alpha.min
    }

}

object ByteComputer17 {

    /*
    Conversion to Assembly
     0: BST A   B = A & 0111b
     2: BXL 5   B = B ^ 0101b
     4: CDV B   C = A / POW(2, B)
     6: BXL 6   B = B ^ 0110b
     8: ADV 3   A = A / 8  (shift >> 3)
    10: BXC 3   B = B ^ C
    12: OUT B   OUT B
    14: JNZ 0   IF A != 0 JUMP 0

    Processing is done in octets of A. Each octet produces one output digit based on the below formula.
    Then A is divided by 8 which is the equivalent of shift right by 3 or throw away the right-most octet
     */

    def processOctalDigits(init: Long): Long = {
        var (a, b, c) = (0L, 0L, 0L)
        a = init
        b = a % 8
        b = (b ^ 0b101) & 0b111
        c = a / pow(2.0, b.toDouble).toLong
        b = (b ^ 0b110) & 0b111
        b = (b ^ c) & 0b111
        b
    }

    def toAssembly(prog: Vector[String]): List[String] = {
        prog.sliding(2, 2).zipWithIndex
            .map( e => f"${e._2 * 2}%2d: ${mapOpCode(e._1.head.toInt)} ${mapParam(e._1.head.toInt, e._1(1).toInt)}").toList
    }

    private def mapOpCode(op: Int): String = opCodeFromByte(op).code

    private def mapParam(opcode: Int, param: Int): String = {
        if Set(1, 3).contains(opcode) then param.toString
        else param match
            case i if i <= 3 => i.toString
            case 4 => "A"
            case 5 => "B"
            case 6 => "C"
    }

    // input parsing
    private def readRegister(s: String): (String, Long) =
        s match { case s"Register ${r}: ${v}" => (r, v.toLong) }

    def toProgram(s: String): Vector[String] =
        s match { case s"Program: ${p}" => p.split(",").toVector }
}
