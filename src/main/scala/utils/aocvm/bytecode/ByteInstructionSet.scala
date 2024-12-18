package org.mpdev.scala.aoc2024
package utils.aocvm.bytecode

import utils.aocvm.OpResultCode.*
import utils.aocvm.OpResultCode
import utils.aocvm.bytecode.ParamMode.{C, L, NA}

import scala.collection.mutable
import scala.math.pow

object InstructionSet {

    // default instruction set
    val ADV = 0
    val BXL = 1
    val BST = 2
    val JNZ = 3
    val BXC = 4
    val OUT = 5
    val BDV = 6
    val CDV = 7

    val REG_NA = "_"

    /*
    There are two types of operands; each instruction specifies the type of its operand.
    The value of a literal operand is the operand itself. For example, the value of the literal operand 7 is the number 7
    The value of a combo operand can be found as follows:

    Combo operands 0 through 3 represent literal values 0 through 3.
    Combo operand 4 represents the value of register A.
    Combo operand 5 represents the value of register B.
    Combo operand 6 represents the value of register C.
    Combo operand 7 is reserved and will not appear in valid programs.

    The adv instruction (opcode 0) performs division. The numerator is the value in the A register.
    The denominator is found by raising 2 to the power of the instruction's combo operand.
    (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
    The result of the division operation is truncated to an integer and then written to the A register.

    The bxl instruction (opcode 1) calculates the bitwise XOR of register B and the instruction's literal operand,
    then stores the result in register B.

    The bst instruction (opcode 2) calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits),
    then writes that value to the B register.

    The jnz instruction (opcode 3) does nothing if the A register is 0. However, if the A register is not zero,
    it jumps by setting the instruction pointer to the value of its literal operand; if this instruction jumps,
    the instruction pointer is not increased by 2 after this instruction.

    The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C, then stores the result in register B.
    (For legacy reasons, this instruction reads an operand but ignores it.)

    The out instruction (opcode 5) calculates the value of its combo operand modulo 8, then outputs that value.
    (If a program outputs multiple values, they are separated by commas.)

    The bdv instruction (opcode 6) works exactly like the adv instruction except that the result is stored in the B register.
    (The numerator is still read from the A register.)

    The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is stored in the C register.
    (The numerator is still read from the A register.)
     */
    val opCodesList: mutable.Map[Int, OpCode] = mutable.Map(
        ADV -> OpCode("ADV", "A", C, { a => (SET_MEMORY, "A", a.head / pow(2.0, a(1).toDouble).asInstanceOf[Long]) }),
        BDV -> OpCode("BDV", "A", C, { a => (SET_MEMORY, "B", a.head / pow(2.0, a(1).toDouble).asInstanceOf[Long]) }),
        CDV -> OpCode("CDV", "A", C, { a => (SET_MEMORY, "C", a.head / pow(2.0, a(1).toDouble).asInstanceOf[Long]) }),
        JNZ -> OpCode("JNZ", "A", L, { a => if (a.head != 0L) (SET_PC, "_", a(1)) else (NONE, "_", 0) }),
        OUT -> OpCode("OUT", "_", C, { a => (OUTPUT, "_", a.head % 8) }),
        BXL -> OpCode("BXL", "B", L, { a => (SET_MEMORY, "B", a.head ^ a(1)) }),
        BST -> OpCode("BST", "_", C, { a => (SET_MEMORY, "B", a.head % 8) }),
        BXC -> OpCode("BXC", "B,C", NA, { a => (SET_MEMORY, "B", a.head ^ a(1)) }),
    )

    def opCodeFromByte(b: Int): OpCode = opCodesList(b)
}      

case class OpCode(code: String, inpReg: String, paramMode: ParamMode, execute: List[Long] => (OpResultCode, String, Long))

enum ParamMode {
    case L   // Literal
    case C   // Combo
    case NA  // ignore
}


