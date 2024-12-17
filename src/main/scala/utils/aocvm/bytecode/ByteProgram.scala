package org.mpdev.scala.aoc2024
package utils.aocvm.bytecode

import utils.aocvm.OpResultCode.*
import utils.aocvm.ProgramState.COMPLETED
import utils.aocvm.{IoChannel, Program, ProgramState}

import framework.AoCException
import utils.aocvm.bytecode.InstructionSet.{REG_NA, opCodeFromByte}
import utils.aocvm.bytecode.ParamMode.{L, C, NA}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.boundary
import scala.util.boundary.break

class ByteProgram extends Program {

    private var instructionList: ArrayBuffer[Int] = ArrayBuffer()
    private var ioChannel: List[IoChannel[Long]] = List()

    private val registers = mutable.Map[String, Long]().withDefaultValue(0)

    override def newInstance(prog: Vector[String], ioChannel: List[IoChannel[Long]]): Program = {
        instructionList = prog.map(_.toInt).to(ArrayBuffer)
        this.ioChannel = ioChannel
        this
    }

    override def run(initReg: Map[String, Long] = Map(), maxCount: Int = Int.MaxValue): Int = { // Future[Int] = Future[Int] {
        log.info(s"$instanceName started${if (initReg.nonEmpty) ", init registers:" else ""} ${initReg.mkString}")
        var pc: Int = 0
        var outputCount: Int = 0
        registers.clear()
        initReg.foreach((reg, v) => registers(reg) = v)
        boundary:
            while (pc < instructionList.size && outputCount < maxCount) {
                val (instr, param) = (opCodeFromByte(instructionList(pc)), instructionList(pc + 1))
                val mappedParams = mapParams(instr, param)
                log.debug(s"$instanceName pc: $pc instruction: ${instr.code} ${mappedParams.mkString(", ")}")
                val (resCode, reg, value) = instr.execute(mappedParams)
                log.debug(s"$instanceName     result: $resCode $reg $value")
                resCode match {
                    case SET_MEMORY => registers(reg) = valueOf(value)
                    case SET_PC => pc = valueOf(value).toInt - 2
                    case OUTPUT =>
                        log.debug(s"AocProg $instanceName writing to output $value")
                        ioChannel(1).send(valueOf(value))
                        println(s"Out $value")
                        outputCount += 1
                    case EXIT => break()
                    case NONE => ;
                    case _ => ;
                }
                pc += 2
            }
        programState = COMPLETED
        log.info(s"$instanceName completed")
        0
    }
    
    override def getRegister(reg: String): Long = registers(reg)
    override def setRegister(reg: String, value: Long): Unit = registers(reg) = value

    override def getMemory(address: Int): Long = getRegister(address.toString)
    override def setMemory(address: Int, value: Long): Unit = setRegister(address.toString, value)

    private def valueOf(x: Any): Long =
        x match
            case i: Int => i.toLong
            case l: Long => l
            case s: String => registers(s)

    private def mapParams(instr: OpCode, param: Int): List[Any] =
        val newParams = ArrayBuffer[Any]()
        if instr.inpReg != REG_NA then
            instr.inpReg.split(",").foreach(r => newParams += valueOf(r))
        newParams += (
            instr.paramMode match
                case L => valueOf(param)
                case C =>
                    if param <= 3 then  valueOf(param)
                    else if param <= 6 then valueOf(('A' + param - 4).toChar.toString)
                    else throw AoCException(s"invalid value of Combo parameter: $param")
                case NA => ;
            )
        newParams.toList

}
