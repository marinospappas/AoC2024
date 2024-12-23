package org.mpdev.scala.aoc2024
package graph

import utils.{Dijkstra, DjikstraV0, Graph}

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDijkstra extends AnyFlatSpec {

    val log: Logger = LoggerFactory.getLogger(classOf[TestDijkstra])
    
    val graph: Graph[String] = Graph()
    {
        graph.addNodesWithCost("1", Set( ("2", 1), ("5", 5) ))
        graph.addNodesWithCost("2", Set( ("3", 2), ("7", 10) ))
        graph.addNodesWithCost("3", Set( ("4", 3), ("7", 8) ))
        graph.addNodesWithCost("4", Set( ("7", 1) ))
        graph.addNodesWithCost("5", Set( ("6", 2) ))
        graph.addNodesWithCost("6", Set( ("7", 1) ))
        graph.addNodesWithCost("7", Set())
    }
    it should "find minimum path in simple graph" in {
        println(graph)
        val result = Dijkstra[String](graph).minPath("1", id => id == "7")
        println(result)
        (result.head._2, result.size) shouldBe (7, 5)
    }

    val graph1: Graph[String] = Graph()
    {
        graph1.addNode("A", "B", true)
        graph1.addNode("B", "C", true)
        graph1.addNode("C", "A", true)
    }
    it should "find paths that ends in start" in {
        println(graph1)
        val result = Dijkstra[String](graph1).allPaths("A", id => id == "A")
        println(result)
        //(result.head., result.size) shouldBe (3, 4)
    }

    
}
