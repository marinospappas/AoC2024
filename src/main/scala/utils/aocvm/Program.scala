package org.mpdev.scala.aoc2024
package utils.aocvm

import OpResultCode.*
import utils.aocvm.ProgramState.READY

import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable
import scala.concurrent.Future

abstract class Program {

    val log: Logger = LoggerFactory.getLogger(classOf[Program])
    var programState: ProgramState = READY
    var instanceName = ""

    private val registers = mutable.Map[String, Long]().withDefaultValue(0)

    def newInstance(prog: Vector[String], ioChannel: List[IoChannel[Long]]): Program
        
    def run(initReg: Map[String, Long] = Map(), maxCount: Int = Int.MaxValue): Int // Future[Int]
    
    def getRegister(reg: String): Long
    
    def setRegister(reg: String, value: Long): Unit

    def getMemory(address: Int): Long
    
    def setMemory(address: Int, value: Long): Unit
}

enum ProgramState {
    case READY
    case RUNNING
    case WAIT
    case COMPLETED
}