package org.mpdev.scala.aoc2024
package solutions.day24

import framework.{InputReader, PuzzleSolver}
import solutions.day24.LogicalCircuit.{readGate, readInput}
import solutions.day24.Gate.{AND, OR, XOR}

class LogicalCircuit(testData: Vector[String] = Vector()) extends PuzzleSolver {

    val inputData: Vector[String] = if testData.isEmpty then InputReader.read(24) else testData
    val inputSignals: Map[String, Int] = inputData.splitAt(inputData.indexOf(""))._1.map( readInput ).toMap
    val gates: Map[String, Gate] = inputData.splitAt(inputData.indexOf("") + 1)._2.map( readGate )
        .map(g => (g.id, g)).toMap
    val outputs: Vector[String] = gates.values.map( _.id ).filter( _.startsWith("z") ).toVector.sorted.reverse

    def calculateCircuitOutput(id: String, input: Map[String, Int]): Int = {
        if input.contains(id) then
            input(id)
        else {
            val gate = gates(id)
            gate.function(calculateCircuitOutput(gate.input1, input), calculateCircuitOutput(gate.input2, input))
        }
    }

    def checkOutputForBit(id: String): Boolean = {
        val wire1 = id.replace("z", "x")
        val wire2 = id.replace("z", "y")
        for (inp1, inp2) <- Set((0, 0), (0, 1), (1, 0), (1, 1))
            yield inp1 + inp2
        true
    }

    def printCircuit(output: String, indent: String): Unit = {
        if inputSignals.contains(output) then println(s"$indent$output")
        else
            println(s"$indent${gates(output)}")
            printCircuit(gates(output).input1, indent + "  ")
            printCircuit(gates(output).input2, indent + "  " )
    }

    def printCircuit(): Unit =
        for bit <- outputs.reverse do
            printCircuit(bit, "")
            println()

    override def part1: Any =
        java.lang.Long.parseLong(outputs.map( calculateCircuitOutput(_, inputSignals) ).mkString, 2)

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

/*
class PulseProcessor(input: List<String>) {

    companion object {
        const val broadcaster = "broadcaster"
        private val startPulse = Pulse(PulseType.LOW, "button", broadcaster)
    }
    var debug = false
    var debug2 = false
    private val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(input)
    val modules: Map<String,Module> = aocInputList.associate {
        val id = it.sender.substring(1, it.sender.length)
        when (it.sender.first()) {
            'b' -> broadcaster to Broadcaster(it.receivers)
            '%' -> id to FlipFlop(id, it.receivers)
            '&' -> id to Conjunction(id, it.receivers)
            else -> throw AocException("invalid input data [$it]")
        }
    }
    private val endModule = (modules.values.map { it.destinations }.flatten().distinct().toSet()
            - modules.keys).first()
    private val endStateInputs = mutableListOf<Pair<Int,Pulse>>()
    private var cycleCount = 0

    init {
        updateConjState()
    }

    fun processPulse(watchModuleId: String = ""): Pair<Int,Int> {
        val counts = mutableMapOf(PulseType.LOW to 0, PulseType.HIGH to 0, PulseType.NA to 0)
        counts[startPulse.hl] =  counts[startPulse.hl]?.plus(1)!!
        val queue = ArrayDeque<Pulse>().also { q -> q.add(startPulse) }
        while (queue.isNotEmpty()) {
            val inputPulse = queue.removeFirst()
            val curModuleId = inputPulse.destination
            if (curModuleId == watchModuleId)
                endStateInputs.add(Pair(cycleCount, inputPulse))
            val outputPulses = modules[curModuleId]?.sendPulses(inputPulse)!!
            if (debug) println("module $curModuleId received ${inputPulse.hl} from ${inputPulse.sender} - ${modules[curModuleId]}")
            for (pulse in outputPulses) {
                counts[pulse.hl] = counts[pulse.hl]?.plus(1)!!
                if (pulse.destination == endModule)
                    continue
                if (pulse.hl != PulseType.NA)
                    queue.add(pulse)
            }
        }
        return Pair(counts[PulseType.LOW]!!, counts[PulseType.HIGH]!!)
    }

    fun countPulsesPart1(): Int {
        var countL = 0
        var countH = 0
        repeat(1000) {
            processPulse().let { countL += it.first; countH += it.second }
        }
        return countL * countH
    }

    fun identifyHighPulseCyclesForFinalConjunction(): Set<Long> {
        // this is the conjunction module that feeds the final module
        val moduleToWatch = modules.values.first { it.destinations.contains(endModule) }.id
        repeat(20000) {
            cycleCount = it + 1
            processPulse(moduleToWatch)
        }
        val inputCycles = endStateInputs.filter { it.second.hl == PulseType.HIGH }.groupBy { it.second.sender }
        if (debug2) inputCycles.forEach { it.println() }
        if (inputCycles.entries.size == (modules[moduleToWatch] as Conjunction).inputs.size &&
            inputCycles.values.all { it.size >= 3 && it[1].first == it[0].first * 2 && it[2].first == it[0].first * 3 } )
            return inputCycles.values.map { it[0].first.toLong() }.toSet()
        throw AocException("could not identify cycle of high signals")
    }

    private fun updateConjState() {
        modules.values.filterIsInstance<Conjunction>().forEach { conj ->
            modules.values.forEach { mod ->
                if (mod.destinations.contains(conj.id))
                    conj.inputs[mod.id] = PulseType.LOW
            }
        }
    }
}

 */