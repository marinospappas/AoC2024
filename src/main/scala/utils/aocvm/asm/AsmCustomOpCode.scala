package org.mpdev.scala.aoc2024
package utils.aocvm.asm

import utils.aocvm.asm.OpCode

import scala.collection.mutable.ArrayBuffer

trait AsmCustomOpCode {
    def execute(instructionList: ArrayBuffer[(OpCode, List[Any])], pc: Int, params: List[Any]): Unit
}
