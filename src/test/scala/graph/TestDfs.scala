package org.mpdev.scala.aoc2024
package graph

import utils.{Djikstra, Graph}
import org.mpdev.scala.aoc2015.utils.Dfs

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import org.slf4j.{Logger, LoggerFactory}

class TestDfs extends AnyFlatSpec {

    val log: Logger = LoggerFactory.getLogger(classOf[TestDfs])
    
    val graph: Graph[String] = Graph()
    {
        graph.addNodesWithCost("1", Set( ("2", 1), ("5", 5) ))
        graph.addNodesWithCost("2", Set( ("3", 2), ("7", 10) ))
        graph.addNodesWithCost("3", Set( ("4", 3), ("7", 8) ))
        graph.addNodesWithCost("4", Set( ("7", 1) ))
        graph.addNodesWithCost("5", Set( ("2", 3), ("6", 2) ))
        graph.addNodesWithCost("6", Set( ("7", 1) ))
        graph.addNodesWithCost("7", Set())
        graph.printIt()
    }

    it should "find minimum path in simple graph" in {
        val result = Dfs[String](graph).shortestPath("1", "7")
        println(result)
        result._1 shouldBe 7
    } 
}
