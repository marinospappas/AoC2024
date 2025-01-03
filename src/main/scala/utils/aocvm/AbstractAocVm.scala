package org.mpdev.scala.aoc2024
package utils.aocvm

import utils.aocvm.ProgramState.COMPLETED
import utils.aocvm.AbstractAocVm.WAIT_PRG_TIMEOUT

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

abstract class AbstractAocVm(instructionList: Vector[String], program: Program, val instanceNamePrefix: String) {

    val log: Logger = LoggerFactory.getLogger(classOf[AbstractAocVm])

    // the AoCVM "process table"
    private val instanceTable: ArrayBuffer[AocInstance] = ArrayBuffer[AocInstance]()

    {
        instanceTable.clearAndShrink()
    }

    protected def setupNewInstance(instructionList: Vector[String]): Int =
        val ioChannels = List[IoChannel[Long]](IoChannel[Long](), IoChannel[Long]())
        instanceTable += AocInstance(program.newInstance(instructionList, ioChannels), ioChannels)
        val programId = instanceTable.size - 1
        instanceTable(programId).program.instanceName = s"$instanceNamePrefix-$programId"
        log.info(s"AocCode instance [$programId] configured")
        programId

    protected def aocCtl(programId: Int, cmd: AocCmd, value: Any): Unit =
        ;
        // todo: cmd match
        //           case AocCmd.SET_OUTPUT_BUFFER_SIZE => instanceTable(programId).ioChannels(1) = Channel(value.asInstanceOf[Int])

    /// protected / internal functions
    def runAocProgram(programId: Int, initReg: Map[String, Long] = Map()): Future[Int] =
        instanceTable(programId).job = instanceTable(programId).program.run(initReg)
        instanceTable(programId).job

    def runAocProgramAndWait(programId: Int, initReg: Map[String, Long] = Map(), timeout: Int): Unit =
        Await.result(runAocProgram(programId, initReg), Duration.apply(if timeout > 0 then timeout else WAIT_PRG_TIMEOUT, TimeUnit.MILLISECONDS))

    protected def aocProgramIsRunning(programId: Int): Boolean =
        !instanceTable(programId).job.isCompleted

    protected def waitForAocProgram(programId: Int, timeout: Int): Int = {
        try {
            Await.result(instanceTable(programId).job, Duration.apply(if timeout > 0 then timeout else WAIT_PRG_TIMEOUT, TimeUnit.MILLISECONDS))
        } catch {
            case e: Exception => log.error(s"exception occurred: ${e.getMessage}")
                -1
        }
    }

    protected def setProgramInput(data: List[Long], programId: Int): Unit =
        log.debug(s"set program input to ${data.mkString(", ")}")
        setInputValues(data, instanceTable(programId).ioChannels.head)

    protected def getProgramFinalOutputLong(programId: Int): List[Long] =
        log.debug("getProgramFinalOutputLong called")
        Thread.sleep(1)      // required in case the program job is still waiting for input
        //waitForAocProgram(programId, 0)
        val output = getOutputValues(instanceTable(programId).ioChannels(1))
        log.debug(s"returning output: ${output.mkString(", ")}")
        output

    protected def getProgramAsyncOutputLong(programId: Int): List[Long] =
        log.debug(s"getProgramAsyncOutputLong called, is channel empty ${instanceTable(programId).ioChannels(1).isEmpty}")
        val outputValues = ArrayBuffer[Long]()
        outputValues += instanceTable(programId).ioChannels(1).receive
        while !instanceTable(programId).ioChannels(1).isEmpty do
            outputValues += instanceTable(programId).ioChannels(1).receive
        log.debug(s"returning output: ${outputValues.mkString(", ")}")
        outputValues.toList

    private def setInputValues(values: List[Long], inputChannel: IoChannel[Long]): Unit =
        values.foreach(inputChannel.send)

    private def getOutputValues(outputChannel: IoChannel[Long]): List[Long] =
        val outputValues = ArrayBuffer[Long]()
        outputValues += outputChannel.receive
        while !outputChannel.isEmpty do
            outputValues += outputChannel.receive
        log.debug(s"returning output: ${outputValues.mkString(", ")}")
        outputValues.toList

    protected def setProgramMemory(programId: Int, address: Int, data: Long): Unit =
        instanceTable(programId).program.setMemory(address, data)

    protected def setProgramMemory(programId: Int, address: Int, data: Int): Unit =
        setProgramMemory(programId, address, data.toLong)

    protected def getProgramMemoryLong(programId: Int, address: Int): Long =
        instanceTable(programId).program.getMemory(address)

    protected def getProgramMemory(programId: Int, address: Int): Int =
        getProgramMemoryLong(programId, address).toInt

    protected def getProgramRegisterLong(programId: Int, reg: String): Long =
        instanceTable(programId).program.getRegister(reg)

    protected def getProgramRegister(programId: Int, reg: String): Int =
        getProgramRegisterLong(programId, reg).toInt

    private case class AocInstance(program: Program, ioChannels: List[IoChannel[Long]], var job: Future[Int] | Null = null)

    enum AocCmd {
        case SET_OUTPUT_BUFFER_SIZE
    }
}

object AbstractAocVm {
    val DEF_PROG_INSTANCE_PREFIX: String = "aocprog"
    val WAIT_PRG_TIMEOUT = 1000
}