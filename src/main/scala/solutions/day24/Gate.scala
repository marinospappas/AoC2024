package org.mpdev.scala.aoc2024
package solutions.day24

import scala.collection.mutable

abstract class Gate1(val id: String, val destinations: List[String]) {
    def output(inSignal: Signal): Int
    def sendPulses(inSignal: Signal): List[Signal] = {
        val outSignal = output(inSignal)
        destinations.map ( dest => Signal(outSignal, id, dest) )
    }
    override def toString: String = s"[$id], xmits to: $destinations"
}

class And(id: String, receivers: List[String]) extends Gate1(id, receivers) {
    private val inputs = mutable.Map[String, Int]()
    override def output(inSignal: Signal): Int = {
        inputs(inSignal.sender) = inSignal.value
        if inputs.values.forall ( value => value == 1 ) then 1 else 0
    }
    override def toString = s"AND ${super.toString()} inputs: $inputs"
}

class Or(id: String, receivers: List[String]) extends Gate1(id, receivers) {
    private val inputs = mutable.Map[String, Int]()
    override def output(inSignal: Signal): Int = {
        inputs(inSignal.sender) = inSignal.value
        if inputs.values.forall(value => value == 0) then 0 else 1
    }
    override def toString = s"OR ${super.toString()} inputs: $inputs"
}

class Xor(id: String, receivers: List[String]) extends Gate1(id, receivers) {
    private val inputs = mutable.Map[String, Int]()
    override def output(inSignal: Signal): Int = {
        inputs(inSignal.sender) = inSignal.value
        if inputs.values.forall(value => value == 0) then 0 else 1
    }
    override def toString = s"XOR ${super.toString()} inputs: $inputs"
}

case class Signal(value:Int, sender: String, destination: String)
