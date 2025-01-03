package org.mpdev.scala.aoc2024
package utils.aocvm.asm

import utils.aocvm.OpResultCode.*
import utils.aocvm.ProgramState.{COMPLETED, RUNNING, WAIT}
import utils.aocvm.{IoChannel, Program, ProgramState}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.boundary
import scala.util.boundary.break

class AsmProgram extends Program {
    
    private var instructionList: ArrayBuffer[(OpCode, List[Any])] = ArrayBuffer()
    private var ioChannel: List[IoChannel[Long]] = List()

    private val registers = mutable.Map[String, Long]().withDefaultValue(0)

    override def newInstance(prog: Vector[String], ioChannel: List[IoChannel[Long]]): Program = {
        val sourcePgmOptions: Map[String, String] =
            if (prog.nonEmpty && prog.head.startsWith("#!"))
                prog.head.substring(2).split(",").map(s => {
                    val a = s.split("=")
                    a(0) -> a(1)
                }).toMap
            else
                Map()
        instructionList = prog.filterNot(line => line.startsWith("#") || line.isEmpty)
            .map(_.substring(if (sourcePgmOptions.get("indent") == Option.empty) 0 else sourcePgmOptions("indent").toInt))
            .map(_.split(" "))
            .map(x => (InstructionSet.opCodeFomString(x(0)), x.toList.slice(1, x.length).map(v => v.toIntOrString)))
            .to(ArrayBuffer)
        this.ioChannel = ioChannel
        this
    }

    override def run(initReg: Map[String, Long] = Map(), maxCount: Int = Int.MaxValue): Future[Int] = Future[Int] {
        log.info(s"$instanceName started${if (initReg.nonEmpty) ", init registers:" else ""} ${initReg.mkString}")
        var pc: Int = 0
        var outputCount: Int = 0
        registers.clear()
        initReg.foreach((reg, v) => registers(reg) = v)
        boundary:
            while (pc < instructionList.size && outputCount < maxCount) {
                val (instr, params) = instructionList(pc)
                val mappedParams = mapParams(params, instr.paramMode, instr.numberOfParams)
                log.debug(s"$instanceName pc: $pc instruction: ${instr.code} ${mappedParams.mkString(", ")}")
                val (resCode, values) = instr.execute(mappedParams)
                log.debug(s"$instanceName     result: $resCode ${values.mkString(", ")}")
                resCode match {
                    case SET_MEMORY => registers(values.head.asInstanceOf[String]) = valueOf(values(1))
                    case INCR_PC => pc += valueOf(values.head).toInt - 1
                    case OUTPUT =>
                        log.debug(s"AocProg $instanceName writing to output ${values.head}")
                        ioChannel(1).send(valueOf(values.head))
                        outputCount += 1
                    case INPUT =>
                        programState = WAIT
                        log.debug(s"AocProg $instanceName waiting for input will be stored in ${values.head}")
                        // will be suspended below and the value in retCode may change
                        val inputValue = ioChannel.head.receive
                        registers(values.head.asInstanceOf[String]) = inputValue
                        programState = RUNNING
                        log.debug(s"AocProg $instanceName received input $inputValue to be stored in ${values.head}")
                    case CUSTOM => values.head.asInstanceOf[AsmCustomOpCode].execute(instructionList, pc, values)
                    case EXIT => break()
                    case NONE => ;
                }
                pc += 1
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

    extension (s: String)
        private def toIntOrString: Any = try
            s.toInt
        catch
            case e: Exception => s

    private def mapParams(params: List[Any], paramMode: List[ParamReadWrite], numberOfParams: Int): List[Any] =
        val newParams = ArrayBuffer[Any]()
        // if the number of params is > than the number if the input params then the first param is added as last as well (to be used as output)
        val thisParams = if (numberOfParams > params.size) params :+ params.head else params
        for (i <- thisParams.indices) do
            // R params are translated to value - W params are left as they are
            newParams += (if (paramMode(i) == ParamReadWrite.R) valueOf(thisParams(i)) else thisParams(i))
        newParams.toList

}
