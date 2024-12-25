package org.mpdev.scala.aoc2024
package solutions.day24

import framework.{InputReader, PuzzleSolver}
import solutions.day24.LogicalCircuit.{readGate, readInput}
import solutions.day24.Gate.{AND, OR, XOR}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import scala.util.boundary.break

class LogicalCircuit(testData: Vector[String] = Vector()) extends PuzzleSolver {

    val inputData: Vector[String] = if testData.isEmpty then InputReader.read(24) else testData
    val inputSignals: Map[String, Int] = inputData.splitAt(inputData.indexOf(""))._1.map( readInput ).toMap
    val gates: Map[String, Gate] = inputData.splitAt(inputData.indexOf("") + 1)._2.map( readGate )
        .map(g => (g.id, g)).toMap
    val outputs: Vector[String] = gates.values.map( _.id ).filter( _.startsWith("z") ).toVector.sorted.reverse

    def calculateCircuitOutput(id: String, input: Map[String, Int], gates: Map[String, Gate]): Int = {
        //if input.contains(id) then
        if id.startsWith("x") || id.startsWith("y") then
            input(id)
        else {
            val gate = gates(id)
            gate.function(calculateCircuitOutput(gate.input1, input, gates), calculateCircuitOutput(gate.input2, input, gates))
        }
    }

    def checkOutputForBit(id: String, gates: Map[String, Gate]): Boolean = {
        val wire1 = id.replace("z", "x")
        val wire2 = id.replace("z", "y")
        val indx = id.replace("z", "").toInt
        val carryInValues = if indx == 0 then Vector(0) else Vector(0, 1)
        val (inp1Values, inp2Values) = (Vector(0, 1), Vector(0, 1))
        (for carryIn <- carryInValues yield
            for inp1 <- inp1Values yield
                for inp2 <- inp2Values yield {
                    val sum = (inp1 ^ inp2) ^ carryIn
                    val carryOut = (inp1 & inp2) | ((inp1 & inp2) | (carryIn & (inp1 ^ inp2)))
                    val inputs = Map(
                        f"x${indx - 1}%02d" -> 1, f"y${indx - 1}%02d" -> carryIn,
                        f"x$indx%02d" -> inp1, f"y$indx%02d" -> inp2
                    ).withDefault(_ => 0)
                    if calculateCircuitOutput(id, inputs, gates) == sum &&
                        (if indx == outputs.size -1 then true
                        else calculateCircuitOutput(f"z${indx + 1}%02d", inputs, gates) == carryOut)
                    then true
                    else false
                })
            .flatten.flatten.forall( _ == true )
    }

    def getAllGatesForBit(id: String): Vector[String] = {
        val result = ArrayBuffer[String]()
        result += id
        if !inputSignals.contains(id) then {
            result ++= getAllGatesForBit(gates(id).input1)
            result ++= getAllGatesForBit(gates(id).input2)
        }
        result.sorted.reverse.toVector
    }

    private def reverseGates(gates: mutable.Map[String, Gate], pairToReverse: List[String]): Unit = {
        val tempId = "____"
//        gates.foreach( e => if e._2.input1 == pairToReverse.head then e._2.input1 = tempId)
//        gates.foreach( e => if e._2.input2 == pairToReverse.head then e._2.input2 = tempId)
//
//        gates.foreach( e => if e._2.input1 == pairToReverse.last then e._2.input1 = pairToReverse.head)
//        gates.foreach( e => if e._2.input2 == pairToReverse.last then e._2.input2 = pairToReverse.head)
//
//        gates.foreach( e => if e._2.input1 == tempId then e._2.input1 = pairToReverse.last)
//        gates.foreach( e => if e._2.input2 == tempId then e._2.input2 = pairToReverse.last)
    }

    def findReversedPair(tryGates: Set[String], bit: String): List[String] = {
        boundary:
            tryGates.toList.combinations(2).foreach( gatesPair =>
                val thisGates = mutable.Map.from(gates)
                reverseGates(thisGates, gatesPair)
                reverseGates(thisGates, (tryGates -- gatesPair.toSet).toList)
                if checkOutputForBit(bit, thisGates.toMap)
                    then break(gatesPair)
            )
            List()
    }

    def printCircuit(output: String, indent: String): Unit = {
        if !inputSignals.contains(output) then {
            println(s"$indent${gates(output)}")
            printCircuit(gates(output).input1, indent + "  ")
            printCircuit(gates(output).input2, indent + "  ")
        }
    }

    def printCircuit(): Unit = {
        for bit <- outputs.reverse do
            printCircuit(bit, "")
            println()
    }

    override def part1: Any =
        java.lang.Long.parseLong(outputs.map( calculateCircuitOutput(_, inputSignals, gates) ).mkString, 2)

    override def part2: Any =
        0
}

object LogicalCircuit {

    // input parsing
    private def readInput(s: String): (String, Int) =
        val matched = """([xy]\d{2}): (\d)""".r.findFirstMatchIn(s).get
        (matched.group(1), matched.group(2).toInt)

    private def readGate(s: String): Gate = {
        val matched = """([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)""".r.findFirstMatchIn(s).get
        val (in1, in2, out) = (matched.group(1), matched.group(3), matched.group(4))
        matched.group(2) match
            case "AND" => AND(out, in1, in2)
            case "OR" => OR(out, in1, in2)
            case "XOR" => XOR(out, in1, in2)
    }
}
