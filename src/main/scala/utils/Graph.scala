package org.mpdev.scala.aoc2024
package utils

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

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

    override def toString: String = {
        val sb = StringBuilder()
        var count = 0
        sb.append("[Graph]\n")
        nodes.foreach(e =>
            count += 1
            sb.append(s"node $count: ${e._1} connected to: ${e._2}\n")
        )
        sb.toString()
    }
    
    def printIt(): Unit = println(this.toString)

    def longestPathDfs(start: T, includeAllNodes: Boolean = true, isAtEnd: T => Boolean): Int =
        dfsMaxPath(start, isAtEnd, mutable.Map(), includeAllNodes)

    //TODO: refactor the below function to use Stack instead of recursion
    private def dfsMaxPath(cur: T, isAtEnd: T => Boolean, visited: mutable.Map[T, Int], includeAllNodes: Boolean): Int = {
        if (isAtEnd(cur) && (if (includeAllNodes) visited.size == nodes.size else visited.nonEmpty))
            return visited.values.sum()

        var maxPath = Int.MinValue
        getConnected(cur).foreach((neighbor, steps) =>
            if (!visited.contains(neighbor))
                visited += neighbor -> steps
                val res = dfsMaxPath(neighbor, isAtEnd, visited, includeAllNodes)
                if (res > maxPath)
                    maxPath = res
                visited.remove(neighbor)
        )
        maxPath
    }

    // topological sort of the whole graph with cycle detection
    def topologicalSort(cycles: ArrayBuffer[List[T]] | Null = null): List[T] = {
        val stack: mutable.Stack[T] = mutable.Stack()
        val visited: mutable.Set[T] = mutable.Set()
        for (node <- getNodes) do {
            if !visited.contains(node) then
                topologicalSortDfs(node, visited, stack)
        }
        if cycles != null then {    // detect cycles
            getConnected(stack.last).map(_._1).foreach(cnx =>
                if stack.contains(cnx) then cycles += (stack.toList :+ cnx)
            )
        }
        stack.toList
    }

    // topological sort of part of the graph starting at specific node
    def topologicalSort(node: T): List[T] = {
        val stack: mutable.Stack[T] = mutable.Stack()
        val visited: mutable.Set[T] = mutable.Set()
        topologicalSortDfs(node, visited, stack)
        stack.toList
    }
    
    private def topologicalSortDfs(node: T, visited: mutable.Set[T], stack: mutable.Stack[T]): Unit = {
        visited.add(node)
        for cnx <- getConnected(node).map(_._1) do {
            if !visited.contains(cnx) then
                topologicalSortDfs(cnx, visited, stack)
        }
        stack.push(node)
    }
}

object Graph {
    var aStarAlgorithm = false // flag used to distinguish between A* and Dijkstra algorithm for min cost path
}
