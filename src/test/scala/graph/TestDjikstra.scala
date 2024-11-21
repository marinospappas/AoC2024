package org.mpdev.scala.aoc2024
package graph

import utils.{Djikstra, Graph}

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDjikstra extends AnyFlatSpec {

    val log: Logger = LoggerFactory.getLogger(classOf[TestDjikstra])
    
    val graph: Graph[String] = Graph()
    {
        graph.addNodesWithCost("1", Set( ("2", 1), ("5", 5) ))
        graph.addNodesWithCost("2", Set( ("3", 2), ("7", 10) ))
        graph.addNodesWithCost("3", Set( ("4", 3), ("7", 8) ))
        graph.addNodesWithCost("4", Set( ("7", 1) ))
        graph.addNodesWithCost("5", Set( ("6", 2) ))
        graph.addNodesWithCost("6", Set( ("7", 1) ))
        graph.addNodesWithCost("7", Set())
        graph.printIt()
    }

    it should "find minimum path in simple graph" in {
        val result = Djikstra[String](graph).minPath("1", id => id == "7")
        result.printPath()
        (result.minCost, result.path.size) shouldBe (7, 5)
    }
    
}
