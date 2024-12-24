package org.mpdev.scala.aoc2024
package solutions.day23

import framework.{InputReader, PuzzleSolver}
import solutions.day23.InterconnectedComputers.readCnx

import scala.collection.immutable.HashSet
import scala.collection.mutable
import scala.util.boundary
import scala.util.boundary.break

class InterconnectedComputers extends PuzzleSolver {

    val inputData: Vector[(String, String)] = InputReader.read(23).map( readCnx )
    val connections: Set[Set[String]] = Set.from(inputData.map( x => Set(x._1, x._2)))
    val allIds: Set[String] = connections.map( _.head )

    var connectionsN: Set[Set[String]] = Set.from(connections)
    private var groupSize: Int = 2

    def findConnectedSetsN(n: Int): Unit = {
        boundary:
            while groupSize < n do {
                val result = mutable.Set[Set[String]]()
                for conx <- connectionsN do {
                    for id <- allIds -- conx do
                        if conx.forall(c => connections.contains(Set(id, c))) then
                            result.add(conx + id)
                }
                if result.isEmpty then break()
                connectionsN = Set.from(result)
                groupSize += 1
                println(s"group size: $groupSize")
            }
    }

    override def part1: Any = {
        findConnectedSetsN(3)
        connectionsN.count(set => set.exists(_.startsWith("t")))
    }

    override def part2: Any = {
        findConnectedSetsN(connectionsN.size)
        connectionsN.flatten.toList.sorted.mkString(",")
    }

}

object InterconnectedComputers {
    // input parsing
    private def readCnx(s: String): (String, String) =
        s match { case s"${s1}-${s2}" => (s1, s2) }

}
