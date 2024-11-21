package org.mpdev.scala.aoc2024
package utils.aocvm

import utils.aocvm.OpResultCode.*
import utils.aocvm.ParamReadWrite.{R, W}

import scala.collection.mutable

object InstructionSet {

    // default instruction set
    val MOV = "mov"
    val ADD = "add"
    val SUB = "sub"
    val MUL = "mul"
    val DIV = "div"
    val MOD = "mod"
    val INC = "inc"
    val DEC = "dec"
    val JMP = "jmp"
    val JNZ = "jnz"
    val IN = "in"
    val OUT = "out"
    val NOP = "nop"
    val HLT = "hlt"

    val opCodesList: mutable.Map[String, OpCode] = mutable.Map(
        MOV -> OpCode(MOV, 2, List(W, R), { a => (SET_MEMORY, List(a.head, a(1).asInstanceOf[Long])) }),
        ADD -> OpCode(ADD, 3, List(R, R, W), { a => (SET_MEMORY, List(a(2), a.head.asInstanceOf[Long] + a(1).asInstanceOf[Long])) }),
        SUB -> OpCode(SUB, 3, List(R, R, W), { a => (SET_MEMORY, List(a(2), a.head.asInstanceOf[Long] - a(1).asInstanceOf[Long])) }),
        MUL -> OpCode(MUL, 3, List(R, R, W), { a => (SET_MEMORY, List(a(2), a.head.asInstanceOf[Long] * a(1).asInstanceOf[Long])) }),
        DIV -> OpCode(DIV, 3, List(R, R, W), { a => (SET_MEMORY, List(a(2), a.head.asInstanceOf[Long] / a(1).asInstanceOf[Long])) }),
        MOD -> OpCode(MOD, 3, List(R, R, W), { a => (SET_MEMORY, List(a(2), a.head.asInstanceOf[Long] % a(1).asInstanceOf[Long])) }),
        INC -> OpCode(INC, 2, List(R, W), { a => (SET_MEMORY, List(a(1), a.head.asInstanceOf[Long] + 1L)) }),
        DEC -> OpCode(DEC, 2, List(R, W), { a => (SET_MEMORY, List(a(1), a.head.asInstanceOf[Long] - 1L)) }),
        JMP -> OpCode(JMP, 1, List(R), { a => (INCR_PC, List(a.head.asInstanceOf[Long])) }),
        JNZ -> OpCode(JNZ, 2, List(R, R), { a => if (a.head.asInstanceOf[Long] != 0L) (INCR_PC, List(a(1).asInstanceOf[Long])) else (NONE, List()) }),
        IN ->  OpCode(IN,  1, List(W), { a => (INPUT, List(a.head)) }),
        OUT -> OpCode(OUT, 1, List(R), { a => (OUTPUT, List(a.head.asInstanceOf[Long])) }),
        NOP -> OpCode(NOP, 0, List(), { a => (NONE, List()) }),
        HLT -> OpCode(HLT, 0, List(), { a => (EXIT, List()) })
    )

    def opCodeFomString(s: String): OpCode = opCodesList(s)
}      

case class OpCode(code: String, numberOfParams: Int, paramMode: List[ParamReadWrite], execute: List[Any] => (OpResultCode, List[Any]))

enum ParamReadWrite {
    case R
    case W
}

enum OpResultCode {
    case SET_MEMORY
    case INCR_PC
    case INPUT
    case OUTPUT
    case EXIT
    case NONE
    case CUSTOM
}