package org.mpdev.scala.aoc2024
package utils

import scala.collection.mutable

/**
 * graph class
 * nodes: map of node_id to (map connected_node_id to weight)
 */
//TODO replace the Int weight with Weight<U> to be able to implement custom compare
open class Graph[T](
                        val nodes: mutable.Map[T, mutable.Map[T, Int]] = mutable.Map[T, mutable.Map[T, Int]](),
                        val customGetConnected: (T => Set[(T, Int)]) | Null = null,
                        val heuristic: (T => Int) | Null = null
                    ) {
    
    def getConnected(id: T): Set[(T, Int)] =
        if (customGetConnected != null)
            customGetConnected(id)
        else
            nodes(id).map(e => (e._1, e._2)).toSet

    def get(id: T): mutable.Map[T, Int] = nodes(id)

    def getNodes: List[T] = nodes.keys.toList

    def getNodesAndConnections: List[(T, mutable.Map[T, Int])] = nodes.toList

    def addNode(id: T): Unit =
        addNodesWithCost(id, Set())

    def addNode(id: T, connected: T, connectBothWays: Boolean = false): Unit =
        addNodesWithCost(id, Set((connected, 1)), connectBothWays)

    def addNodes(id: T, connected: Set[T], connectBothWays: Boolean = false): Unit =
        addNodesWithCost(id, connected.map((_, 1)), connectBothWays)

    def addNodesWithCost(id: T, connected: Set[(T, Int)], connectBothWays: Boolean = false): Unit =
        if (!nodes.contains(id))
            nodes += (id -> mutable.Map())
        connected.foreach( e => nodes(id) += e._1 -> e._2 )
        if (connectBothWays)
            connected.foreach( e => addNodesWithCost(e._1, Set((id, e._2))) )

    def removeConnected(a: T, b: T): Unit = removeConnection((a, b))

    def removeConnection(edge: (T, T)): Unit =
        nodes(edge._1) -= edge._2
        nodes(edge._2) -= edge._1

    def getCost(a: T, b: T): Int = nodes(a)(b)

    def setCost(a: T, b: T, cost: Int): Unit = nodes(a) += b -> cost

    def getAllConnectedPairs: Set[Set[T]] =
        nodes.flatMap(n => n._2.map(c => Set(n._1, c._1))).toSet

    def printIt(): Unit =
        var count = 0
        println("[Graph]")
        nodes.foreach( e =>
                count += 1
                println(s"node $count: ${e._1} connected to: ${e._2}")
        )

    def longestPathDfs(start: T, includeAllNodes: Boolean = true, isAtEnd: T => Boolean): Int =
        dfsMaxPath(start, isAtEnd, mutable.Map(), includeAllNodes)

    //TODO: refactor the below function to use Stack instead of recursion
    private def dfsMaxPath(cur: T, isAtEnd: T => Boolean, visited: mutable.Map[T, Int], includeAllNodes: Boolean): Int =
        if (isAtEnd(cur) && (if (includeAllNodes) visited.size == nodes.size else visited.nonEmpty))
            return visited.values.sum()
            
        var maxPath = Int.MinValue
        getConnected(cur).foreach ( (neighbor, steps) =>
            if (! visited.contains(neighbor))
                visited += neighbor -> steps
                val res = dfsMaxPath(neighbor, isAtEnd, visited, includeAllNodes)
                if (res > maxPath)
                    maxPath = res
                visited.remove(neighbor)
        )
        maxPath
        
}

object Graph {
    var aStarAlgorithm = false // flag used to distinguish between A* and Dijkstra algorithm for min cost path
}
