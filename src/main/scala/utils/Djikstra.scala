package org.mpdev.scala.aoc2024
package utils

import framework.AoCException

import java.util.PriorityQueue
import java.util.ArrayDeque
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import boundary.break
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util

class Djikstra[T](g: Graph[T]) {

    val log: Logger = LoggerFactory.getLogger("Djikstra")

    Graph.aStarAlgorithm = false // ensure dijkstra is used

    private val distFromStart: mutable.Map[T, Int] = mutable.Map()
    private val predecessors: mutable.Map[T, ArrayBuffer[T]] = mutable.Map()
    val visited: ArrayBuffer[PathNode[T]] = ArrayBuffer()
    var iterations = 0

    def allPaths(start: T, isAtEnd: T => Boolean): Vector[Vector[(T, Int)]] = {
        allPaths(start)
        getPaths(start, t => isAtEnd(t))
    }

    /**
     * Dijkstra algorithm for All Paths
     */
    private def allPaths(start: T): Unit = {

        val priorityQueue = PriorityQueue[PathNode[T]]()
        distFromStart.clear()
        predecessors.clear()
        visited.clear()
        distFromStart.put(start, 0)
        predecessors.put(start, ArrayBuffer())
        iterations = 0

        priorityQueue.add(PathNode(start, 0))
        // while the priority Q has elements, get the top one (least cost as per Comparator)
        while (!priorityQueue.isEmpty) {
            val pathNode = priorityQueue.poll()
            val (curNode, curDistance) = (pathNode._1.asInstanceOf[T], pathNode._2)
            boundary:
                log.trace(s"> connected nodes for node $curNode")
                for connectedNode <- g.getConnected(curNode).filter( _._1 != start) do {
                    val nextPathNode = PathNode(connectedNode._1, connectedNode._2)
                    //TODO investigate improvement by implementing visited list
                    //if (visited.contains(nextPathNode))
                    //    break()
                    iterations += 1
                    visited += nextPathNode
                    val totalDistance = curDistance + connectedNode._2
                    if totalDistance < distFromStart.getOrElse(connectedNode._1, Int.MaxValue) then {
                        distFromStart(connectedNode._1) = totalDistance
                        predecessors(connectedNode._1) = ArrayBuffer(curNode)
                        priorityQueue.add(PathNode(connectedNode._1, totalDistance))
                    } else
                        predecessors(connectedNode._1) = predecessors.getOrElse(connectedNode._1, ArrayBuffer()) :+ curNode
                }
        }
    }

    private def getPaths(start: T, isAtEnd: T => Boolean): Vector[Vector[(T, Int)]] = {
        //TODO: the below is actually a List of points that satisfy isAtEnd
        val end = predecessors.keys.find(isAtEnd(_)).get
        val allPaths = ArrayBuffer[ArrayBuffer[(T, Int)]]()
        val queue = util.ArrayDeque[ArrayBuffer[(T, Int)]]()
        var curPath = ArrayBuffer[(T, Int)]((end, distFromStart(end)))
        queue.add(curPath)
        while !queue.isEmpty do {
            curPath = queue.poll()
            val lastNode = curPath.last
            if lastNode._1 == start then
                allPaths += curPath
            else
                predecessors(lastNode._1).foreach(predecessor =>
                    if !curPath.contains(predecessor) && lastNode._2 > distFromStart(predecessor) then {
                        val newPartialPath = curPath.to(ArrayBuffer)
                        newPartialPath += ((predecessor, distFromStart(predecessor)))
                        queue.add(newPartialPath)
                    }
                )
        }
        allPaths.map( _.toVector ).toVector
    }
}

case class PathNode[T](id: T | Null = null, var costFromStart: Int = 0) extends Comparable[PathNode[T]] {
    override def compareTo(other: PathNode[T]): Int = costFromStart.compareTo(other.costFromStart)
}