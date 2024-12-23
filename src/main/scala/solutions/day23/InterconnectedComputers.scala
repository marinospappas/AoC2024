package org.mpdev.scala.aoc2024
package solutions.day23

import framework.{InputReader, PuzzleSolver}
import solutions.day23.InterconnectedComputers.readCnx
import utils.Graph

import java.util
import scala.collection.immutable.HashSet
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import scala.util.boundary.break

class InterconnectedComputers extends PuzzleSolver {

    val inputData: Vector[(String, String)] = InputReader.read(23).map( readCnx )
    val graph: Graph[String] = Graph()
    {
        inputData.foreach(pair => graph.addNode(pair._1, pair._2, true))
    }

    def findConnectedN(n: Int): Set[HashSet[String]] = {
        val result = mutable.Set[HashSet[String]]()
        for node <- graph.getNodes do {
            val stack = util.Stack[ArrayBuffer[String]]()
            stack.push(ArrayBuffer(node))
            while !stack.isEmpty do {
                val currentPath = stack.pop()
                if currentPath.head == currentPath.last && currentPath.size == n + 1 then
                    result.add(HashSet.from(currentPath.slice(0, n)))
                else {
                    graph.getConnected(currentPath.last).foreach( connected =>
                        if currentPath.size <= n then {
                            val newPath = currentPath :+ connected._1
                            stack.push(newPath)
                        }
                    )
                }
            }
        }
        result.toSet
    }

    var connected3: Set[HashSet[String]] = Set()

    override def part1: Any =
        connected3 = findConnectedN(3)
        connected3.count( set => set.exists(_.startsWith("t")) )

    override def part2: Any = {
        var setOfConnectedSets = Set.from(connected3.filter( set => set.exists(_.startsWith("t") ) ) )
        val newSetOfConnectedSets = mutable.Set[HashSet[String]]()
        boundary:
            while setOfConnectedSets.size > 1 do {
                for connectedSet <- setOfConnectedSets do {
                    print(s"${connectedSet.size}...")
                    val remainingComputers = graph.getNodes.toSet -- connectedSet
                    remainingComputers.foreach(comp =>
                        if !connectedSet.contains(comp) && connectedSet.intersect(graph.getConnected(comp).map(_._1)) == connectedSet then
                            newSetOfConnectedSets.add(connectedSet + comp)
                    )
                }
                setOfConnectedSets = HashSet.from(newSetOfConnectedSets)
            }
        setOfConnectedSets.toList.head.toList.sorted.mkString(",")
    }

}

object InterconnectedComputers {

    private def readCnx(s: String): (String, String) =
        s match { case s"${s1}-${s2}" => (s1, s2) }

}
