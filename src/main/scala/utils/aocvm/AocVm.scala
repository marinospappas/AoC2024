package org.mpdev.scala.aoc2024
package utils.aocvm

open class AocVm(instructionList: Vector[String], program: Program, instanceNamePrefix: String = AbstractAocVm.DEF_PROG_INSTANCE_PREFIX) extends
    AbstractAocVm(instructionList, program, instanceNamePrefix) {

    def newProgram: Int = setupNewInstance(instructionList)
    
    def newProgram(instructionList: Vector[String]): Int = setupNewInstance(instructionList)

    def aocCtl(cmd: AocCmd, value: Any, programId: Int = 0): Unit = super.aocCtl(programId, cmd, value)

     def runProgram(initReg: Map[String, Long] = Map(), programId: Int = 0): Unit =
        log.info(s"AocCode instance [$programId] starting")
        runAocProgram(programId, initReg)

     def runProgramAndWait(initReg: Map[String, Long] = Map(), programId: Int = 0, timeout: Int = -1): Unit =
         log.info(s"AocCode instance [$programId] starting")
         runAocProgramAndWait(programId, initReg, timeout)
        
    def waitForProgram(): Unit = waitForProgram(0)

    def waitForProgram(programId: Int): Unit = waitForAocProgram(programId)
    
    //def runProgramAndWait(initReg: Map[String, Long] = Map(), programId: Int = 0): Unit =
    //     log.info(s"AocCode instance [$programId] starting with Wait")
    //     runAocProgramAndWait(programId, initReg)


    def sendInputToProgram(data: Int): Unit = sendInputToProgram(data, 0)
        
     def sendInputToProgram(data: Int, programId: Int): Unit =
        setProgramInput(List(data.toLong), programId)

     def sendInputToProgram(data: List[Int], programId: Int = 0): Unit =
        setProgramInput(data.map( _.toLong ), programId)

     def getFinalOutputFromProgram: List[Int] = getProgramFinalOutputLong(0).map( _.toInt )
     def getAsyncOutputFromProgram(programId: Int = 0): List[Int] = getProgramAsyncOutputLong(programId).map( _.toInt )

     def getFinalOutputFromProgram(programId: Int): List[Int] = getProgramFinalOutputLong(programId).map(_.toInt)

     def getFinalOutputFromProgramLong: List[Long] = getProgramFinalOutputLong(0)
     def getAsyncOutputFromProgramLong(programId: Int = 0): List[Long] = getProgramAsyncOutputLong(programId)

    def programIsRunning(): Boolean = aocProgramIsRunning(0)

    def setProgramMemory(address: Int, data: Long): Unit = setProgramMemory(0, address, data)
    
    def setProgramMemory(address: Int, data: Int): Unit = setProgramMemory(address, data.toLong)

    def getProgramMemoryLong(address: Int): Long = getProgramMemoryLong(0, address)

    def getProgramMemory(address: Int): Int = getProgramMemoryLong(address).toInt

    def getProgramRegisterLong(reg: String): Long = getProgramRegisterLong(0, reg)

    def getProgramRegister(reg: String): Int = getProgramRegisterLong(reg).toInt
    
}