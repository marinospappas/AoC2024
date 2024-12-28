package org.mpdev.scala.aoc2024
package solutions.day24

import framework.{InputReader, PuzzleSolver}
import solutions.day24.LogicalCircuit.{And, Or, Xor, readGate, readInput}
import solutions.day24.Gate.{AND, NONE, OR, XOR}

import scala.collection.mutable

class LogicalCircuit(testData: Vector[String] = Vector()) extends PuzzleSolver {

    val inputData: Vector[String] = if testData.isEmpty then InputReader.read(24) else testData
    private val inputForCircuit = inputData.splitAt(inputData.indexOf("") + 1)._2
    val inputSignals: Map[String, Int] = inputData.splitAt(inputData.indexOf(""))._1.map( readInput ).toMap
    val gates: Map[String, Gate] = inputForCircuit.map( readGate ).map(g => (g.id, g)).toMap
    val outputs: Vector[String] = gates.values.map( _.id ).filter( _.startsWith("z") ).toVector.sorted.reverse

    def calculateCircuitOutput(id: String, input: Map[String, Int], gates: Map[String, Gate]): Int =
        if id.startsWith("x") || id.startsWith("y") then
            input(id)
        else
            gates(id).function(calculateCircuitOutput(gates(id).input1, input, gates),
                calculateCircuitOutput(gates(id).input2, input, gates))

    // was used only in the initial analysis of the circuit
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

    def printCircuit(output: String, indent: String): Unit = {
        if !inputSignals.contains(output) then {
            println(s"$indent${gates(output)}")
            printCircuit(gates(output).input1, indent + "  ")
            printCircuit(gates(output).input2, indent + "  ")
        }
    }

    // was used only in the initial analysis of the circuit
    def printCircuit(): Unit = {
        for bit <- outputs.reverse do
            printCircuit(bit, "")
            println()
    }

    private def identifyGate(inp1: String, inp2: String, gateType: String, circuit: Map[String, Gate]): String = {
        val gate = circuit.values
            .find( gate => Set(gate.input1, gate.input2) == Set(inp1, inp2) && gate.getClass.getSimpleName == gateType)
            .orElse(Option[Gate](NONE)).get.id
        gate
    }

    private def swapOutput(inputData: Vector[String], str1: String, str2: String): Vector[String] =
        inputData.map( s =>
            val a = s.split(" -> ")
            a(1) match
                case s: String if s == str1 => s"${a(0)} -> $str2"
                case s: String if s == str2 => s"${a(0)} -> $str1"
                case _ => s"${a(0)} -> ${a(1)}"
        )

    // the below algorithm is based on the bit adder circuit that adds 2 1-bit numbers with carry bit:
    // input bits: x, y and carry_in
    // output bit: sum and carry_out
    // sum = (x XOR y) XOR carry_in
    // carry_out = (x AND y) OR ((x XOR y) AND carry_in)
    def inspectCircuit: Set[String] = {
        val swappedConnections = mutable.Set[String]()
        var bit = 1
        var input = inputForCircuit
        var circuit = input.map( readGate ).map(g => (g.id, g)).toMap
        val carry0 = identifyGate("x00", "y00", And, circuit)
        var carryGate = carry0
        while bit < outputs.size - 1 do {
            val (x, y, z) = (f"x$bit%02d", f"y$bit%02d", f"z$bit%02d")
            val (xyXorGate, xyAndGate) = (identifyGate(x, y, Xor, circuit), identifyGate(x, y, And, circuit))
            val sumGate = identifyGate(carryGate, xyXorGate, Xor, circuit)
            sumGate match
                case "" =>
                    swappedConnections.addAll(Set(xyXorGate, xyAndGate))
                    input = swapOutput(input, xyXorGate, xyAndGate)
                    circuit = input.map(readGate).map(g => (g.id, g)).toMap
                    bit = 1
                    carryGate = carry0
                case id: String if id != z =>
                    swappedConnections.addAll(Set(z, sumGate))
                    input = swapOutput(input, sumGate, z)
                    circuit = input.map(readGate).map(g => (g.id, g)).toMap
                    bit = 1
                    carryGate = carry0
                case _ => ;
                    val xyAndCarryGate = identifyGate(carryGate, xyXorGate, And, circuit)
                    carryGate = identifyGate(xyAndCarryGate, xyAndGate, Or, circuit)
                    bit += 1
        }
        swappedConnections.toSet
    }

    override def part1: Any =
        java.lang.Long.parseLong(outputs.map( calculateCircuitOutput(_, inputSignals, gates) ).mkString, 2)

    override def part2: Any =
        inspectCircuit.toList.sorted.mkString(",")
}

object LogicalCircuit {

    val Or = "OR"
    val And = "AND"
    val Xor = "XOR"

    // input parsing
    private def readInput(s: String): (String, Int) =
        val matched = """([xy]\d{2}): (\d)""".r.findFirstMatchIn(s).get
        (matched.group(1), matched.group(2).toInt)

    private def readGate(s: String): Gate = {
        val matched = """([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)""".r.findFirstMatchIn(s).get
        val (in1, in2, out) = (matched.group(1), matched.group(3), matched.group(4))
        matched.group(2) match
            case And => AND(out, in1, in2)
            case Or => OR(out, in1, in2)
            case Xor => XOR(out, in1, in2)
    }
}
