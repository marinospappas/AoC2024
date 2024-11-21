package org.mpdev.scala.aoc2024
package utils

import framework.AoCException

import java.util.PriorityQueue
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import boundary.break

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Djikstra[T](g: Graph[T]) {

    val log: Logger = LoggerFactory.getLogger("Djikstra")

    Graph.aStarAlgorithm = false // ensure dijkstra is used

    /**
     * Dijkstra algorithm
     */
    def minPath(start: T, isAtEnd: T => Boolean): MinCostPath[T] = {

        val priorityQueue = PriorityQueue[GraphPathNode[T]]()
        priorityQueue.add(GraphPathNode(start))
        val visited = ArrayBuffer[GraphPathNode[T]]()
        val dijkstraCost = GraphPathMap[T]()
        var currentNode: GraphPathNode[T] = GraphPathNode()

        var iterations = 0
        // while the priority Q has elements, get the top one (least cost as per Comparator)
        while (!priorityQueue.isEmpty) {
            currentNode = priorityQueue.poll()
            val currentNodeId: T = currentNode.id.asInstanceOf[T]
            // if this is the endNode ID, we are done
            if (isAtEnd(currentNodeId)) {
                val minCostPath = MinCostPath[T] ()
                minCostPath.minCost = currentNode.costFromStart
                minCostPath.numberOfIterations = iterations
                minCostPath.path = dijkstraCost.getMinCostPath(currentNodeId, start)
                return minCostPath
            }
            boundary:
                log.trace(s"> connected nodes for node $currentNodeId")
                for (connectedNode <- g.getConnected(currentNodeId)) do
                    log.trace(s">>      $connectedNode")
                    val nextPathNode = GraphPathNode(connectedNode._1, connectedNode._2)
                    if (visited.contains(nextPathNode))
                        break()
                    iterations += 1
                    visited += nextPathNode
                    // calculate the new cost to that node and the new *estimated* total cost to the end node
                    val newCost = currentNode.costFromStart + connectedNode._2
                    // if the new cost is less than what we have already recorded in the map of nodes/costs
                    // update the map with the new costs and "updatedBy" (to be able to back-track the min.cost path)
                    if (newCost < dijkstraCost.pathMap(connectedNode._1).costFromStart)
                        nextPathNode.updatedBy = currentNode.id
                            nextPathNode.costFromStart = newCost
                        dijkstraCost.pathMap(connectedNode._1) = nextPathNode
                        // and put the updated new node back into the priority queue
                        priorityQueue.add(nextPathNode)
        }
        //dijkstraCost.pathMap.foreach( println(_) )
        throw AoCException(s"no path found from $start to endState")
    }
}

case class GraphPathNode[T](id: T | Null = null, var costFromStart: Int = 0, var updatedBy: T | Null = null) extends Comparable[GraphPathNode[T]] {
    private val estTotalCostToEnd: Int = Int.MaxValue    // used in A* algorithm only
    override def compareTo(other: GraphPathNode[T]): Int =
        if (!Graph.aStarAlgorithm)    // in Dijkstra min cost criteria is based on cost from start to this node
            costFromStart.compareTo(other.costFromStart)
        else   // in A* min cost criteria is based on est total cost from start to end
            estTotalCostToEnd.compareTo(other.estTotalCostToEnd)
}

case class GraphPathMap[T](pathMap: mutable.Map[T, GraphPathNode[T]] = mutable.Map[T, GraphPathNode[T]]().withDefaultValue(GraphPathNode(null, Int.MaxValue))) {
    def getMinCostPath(minCostKey: T, startKey: T): List[(T, Int)] =
        var key: T = minCostKey
        val path: ArrayBuffer[(T, Int)] = ArrayBuffer()
        while ( {
            val node = pathMap(key)
            path += ((key, node.costFromStart))
            key = if (node.updatedBy == null) startKey else node.updatedBy.asInstanceOf[T]
            key != startKey
        }) {}
        path += ((key, 0))
        path.toList.reverse
}