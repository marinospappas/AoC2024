package org.mpdev.scala.aoc2024
package solutions.day05

import framework.{InputReader, PuzzleSolver}
import utils.Graph

class PrintingRules extends PuzzleSolver {

    private val inputData: List[String] = InputReader.read(5)
    val orderingRules: List[(Int, Int)] = inputData.slice(0, inputData.indexOf("")).map (readRule)
    val manSections: List[List[Int]] = inputData.slice(inputData.indexOf("") + 1, inputData.size).map (readPagesSection)

    private def graphOf(rules: List[(Int, Int)], constraints: List[Int]): Graph[Int] = {
        val g = Graph[Int]()
        rules.filter ( r => Set(r._1, r._2).forall (constraints.contains) )
            .foreach((v1, v2) => { g.addNode(v1, v2); g.addNode(v2) })
        g
    }

    private def sortWithCustomOrder(n1: Int, n2: Int, order: List[Int]): Boolean =
        order.indexOf(n1) < order.indexOf(n2)

    override def part1: Any =
        manSections.filter (l => orderingRules.forall (r => l.applyRule(r))).map (l => l(l.size / 2)).sum

    override def part2: Any =
        manSections
            .filter (l => orderingRules.exists (r => !l.applyRule(r) ))
            .map (l => {
                val sortOrder = graphOf(orderingRules, l).topologicalSort()
                l.sortWith((i1, i2) => sortWithCustomOrder(i1, i2, sortOrder))
            })
            .map (l => l(l.size / 2)).sum

    // input parsing
    private def readRule(s: String): (Int, Int) =
        s match { case s"${p1}|${p2}" => (p1.toInt, p2.toInt) }

    private def readPagesSection(s: String): List[Int] =
        s.split(",").map(a => a.toInt).toList
}

extension (l: List[Int])
    def applyRule(rule: (Int, Int)): Boolean = {
        val (i1, i2) = (l.indexOf(rule._1), l.indexOf(rule._2))
        i1 < 0 || i2 < 0 || i1 < i2
    }
