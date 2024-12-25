package org.mpdev.scala.aoc2024
package solutions.day24

import scala.collection.mutable

enum Gate(val id: String, val input1: String, val input2: String, val function: (Int, Int) => Int) {
    case AND(override val id: String, override val input1: String, override val input2: String) extends Gate(
        id, input1, input2, (in1, in2) => in1 & in2
    )
    case OR(override val id: String, override val input1: String, override val input2: String) extends Gate(
        id, input1, input2, (in1, in2) => in1 | in2
    )
    case XOR(override val id: String, override val input1: String, override val input2: String) extends Gate(
        id, input1, input2, (in1, in2) => in1 ^ in2
    )
}

abstract class Gate1(val id: String) {
    val destinations: List[String] = List()
    def output(inSignal: Signal): Int
    def sendPulses(inSignal: Signal): List[Signal] = {
        val outSignal = output(inSignal)
        destinations.map ( dest => Signal(outSignal, id, dest) )
    }
    override def toString: String = s"[$id], xmits to: $destinations"
}

class And(id: String) extends Gate1(id) {
    private val inputs = mutable.Map[String, Int]()
    override def output(inSignal: Signal): Int = {
        inputs(inSignal.sender) = inSignal.value.asInstanceOf[Int]
        if inputs.values.forall ( value => value == 1 ) then 1 else 0
    }
    override def toString = s"AND ${super.toString()} inputs: $inputs"
}

class Or(id: String) extends Gate1(id) {
    private val inputs = mutable.Map[String, Int]()
    override def output(inSignal: Signal): Int = {
        inputs(inSignal.sender) = inSignal.value.asInstanceOf[Int]
        if inputs.values.forall(value => value == 0) then 0 else 1
    }
    override def toString = s"OR ${super.toString()} inputs: $inputs"
}

class Xor(id: String) extends Gate1(id) {
    private val inputs = mutable.Map[String, Int]()
    override def output(inSignal: Signal): Int = {
        inputs(inSignal.sender) = inSignal.value.asInstanceOf[Int]
        if inputs.values.forall(value => value == 0) then 0 else 1
    }
    override def toString = s"XOR ${super.toString()} inputs: $inputs"
}

case class Signal(value:Int | Null, sender: String, destination: String)
