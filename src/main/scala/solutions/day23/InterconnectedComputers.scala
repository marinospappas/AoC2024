package org.mpdev.scala.aoc2024
package solutions.day23

import framework.{InputReader, PuzzleSolver}
import solutions.day23.InterconnectedComputers.readCnx
import utils.{Dijkstra, Graph}

import scala.collection.mutable

class InterconnectedComputers extends PuzzleSolver {

    val inputData: Vector[(String, String)] = InputReader.read(23).map( readCnx )
    val graph: Graph[String] = Graph()
    {
        inputData.foreach(pair => graph.addNode(pair._1, pair._2, true))
    }

    override def part1: Any = {
        val connected3 = mutable.Set[Set[String]]()
        graph.getNodes.foreach( node =>
            val allPaths = Dijkstra[String](graph).allPaths(node, id => id == node)
            if allPaths.exists( _.size == 4 ) then
                println(allPaths)
        )
        connected3
    }

    override def part2: Any =
        0
}

object InterconnectedComputers {

    private def readCnx(s: String): (String, String) =
        s match { case s"${s1}-${s2}" => (s1, s2) }

}
