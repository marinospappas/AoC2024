package org.mpdev.scala.aoc2024
package solutions.day07

import framework.{InputReader, PuzzleSolver}

import scala.collection.mutable.ArrayBuffer

class BridgeRepair extends PuzzleSolver {

    val inputData: List[(Long, List[Long])] = InputReader.read(7).map (s => { val (i, l) = parse(s); i -> l})

    //TODO: debug recursive version
    private def matchListR(l: List[Long], expected: Long): Boolean = {
        if l.size == 2 then l.head + l(1) == expected || l.head * l(1) == expected
        else matchList(l.slice(1, l.size), expected - l.head)
            || matchList(l.slice(1, l.size), expected / l.head)
    }

    private def matchList(list: List[Long], expected: Long, part2: Boolean = false): Boolean = {
        var currentCollection = ArrayBuffer[Long](list.head + list(1), list.head * list(1))
        if part2 then currentCollection += (list.head.toString + list(1).toString).toLong
        var l = list.slice(2, list.size)
        while l.nonEmpty do {
            val previous = currentCollection.toList
            currentCollection = previous.flatMap(n =>
                if !part2 then List(n + l.head, n * l.head)
                else List(n + l.head, n * l.head, (n.toString + l.head.toString).toLong)
            ).to(ArrayBuffer)
            l = l.slice(1, l.size)
        }
        currentCollection.contains(expected)
    }

    override def part1: Any =
        val res = inputData.filter((exp, list) => matchList(list, exp))
        res.map(_._1).sum

    override def part2: Any =
        val res = inputData.filter((exp, list) => matchList(list, exp, part2 = true))
        res.map(_._1).sum

    // input parsing
    private def parse(s: String): (Long, List[Long]) =
        s match { case s"${p1}: ${p2}" => p1.toLong -> p2.split(" ").map(_.toLong).toList }
}