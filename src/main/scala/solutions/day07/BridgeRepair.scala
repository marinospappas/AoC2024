package org.mpdev.scala.aoc2024
package solutions.day07

import framework.{InputReader, PuzzleSolver}

import scala.collection.mutable.ArrayBuffer

class BridgeRepair extends PuzzleSolver {

    val inputData: List[(Long, List[Long])] = InputReader.read(7).map (s => { val (i, l) = parse(s); (i, l) })

    private def matchTwo(n1: Long, n2: Long, expected: Long, part2: Boolean): Boolean =
        n1 + n2 == expected || n1 * n2 == expected || (part2 && (n2.toString + n1.toString).toLong == expected)

    private def matchListDp(list: List[Long], expected: Long, part2: Boolean = false): Boolean = {
        list match
            case l if l.size == 2 => matchTwo(l.head, l(1), expected, part2)
            case l => expected > l.head && matchListDp(l.slice(1, l.size), expected - l.head, part2)
                || expected % l.head == 0 && matchListDp(l.slice(1, l.size), expected / l.head, part2)
                || part2 && expected.toString.length > l.head.toString.length && expected.toString.endsWith(l.head.toString)
                && matchListDp(l.slice(1, l.size), expected.toString.substring(0, expected.toString.length - l.head.toString.length).toLong, part2)
    }

    // alternative "naive" algorithm that calculates all the numbers from all the possible combinations of +, * and ||
    // much slower than DP
    private def matchListLoop(list: List[Long], expected: Long, part2: Boolean = false): Boolean = {
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
        val res = inputData.filter((exp, list) => matchListDp(list.reverse, exp))
        res.map(_._1).sum

    override def part2: Any =
        val res = inputData.filter((exp, list) => matchListDp(list.reverse, exp, part2 = true))
        res.map(_._1).sum

    // input parsing
    private def parse(s: String): (Long, List[Long]) =
        s match { case s"${p1}: ${p2}" => p1.toLong -> p2.split(" ").map(_.toLong).toList }
}