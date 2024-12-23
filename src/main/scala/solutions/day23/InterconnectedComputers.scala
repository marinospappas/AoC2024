package org.mpdev.scala.aoc2024
package solutions.day23

import framework.{InputReader, PuzzleSolver}
import solutions.day23.InterconnectedComputers.readCnx
import utils.{Bfs, Dijkstra, Graph}

import java.util
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class InterconnectedComputers extends PuzzleSolver {

    val inputData: Vector[(String, String)] = InputReader.read(23).map( readCnx )
    val graph: Graph[String] = Graph()
    {
        inputData.foreach(pair => graph.addNode(pair._1, pair._2, true))
    }

    def findConnectedN(n: Int): Set[Set[String]] = {
        val result = mutable.Set[Set[String]]()
        for node <- graph.getNodes do {
            val stack = util.Stack[ArrayBuffer[String]]()
            stack.push(ArrayBuffer(node))
            while !stack.isEmpty do {
                val currentPath = stack.pop()
                if currentPath.head == currentPath.last && currentPath.size == n + 1 then
                    result.add(currentPath.slice(0, n).toSet)
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

    override def part1: Any =
        findConnectedN(3).count(set => set.exists(_.startsWith("t")))

    override def part2: Any =
        0
}

object InterconnectedComputers {

    private def readCnx(s: String): (String, String) =
        s match { case s"${s1}-${s2}" => (s1, s2) }

}
