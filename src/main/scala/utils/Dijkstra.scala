package org.mpdev.scala.aoc2024
package utils

import java.util.PriorityQueue
import java.util.ArrayDeque
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util

class Dijkstra[T](g: Graph[T]) {

    val log: Logger = LoggerFactory.getLogger("Djikstra")

    Graph.aStarAlgorithm = false // ensure dijkstra is used

    private val distFromStart: mutable.Map[T, Int] = mutable.Map()
    private val predecessors: mutable.Map[T, ArrayBuffer[T]] = mutable.Map()
    val visited: mutable.Set[PathNode[T]] = mutable.Set()
    var iterations = 0

    def minPath(start: T, isAtEnd: T => Boolean): Vector[(T, Int)] = {
        findPath(start)
        if getPaths(start, t => isAtEnd(t)).isEmpty then Vector()
        else getPaths(start, t => isAtEnd(t)).head
    }

    def allMinPaths(start: T, isAtEnd: T => Boolean): Vector[Vector[(T, Int)]] = {
        findPath(start, allPaths = true)
        val allPaths = getPaths(start, t => isAtEnd(t))
        if allPaths.isEmpty then Vector()
        else {
            val minCost = allPaths.map( _.head._2 ).min
            allPaths.filter(p => p.head._2 == minCost)
        }
    }

    def allPaths(start: T, isAtEnd: T => Boolean): Vector[Vector[(T, Int)]] = {
        findPath(start, allPaths = true)
        getPaths(start, t => isAtEnd(t))
    }

    /**
     * Dijkstra algorithm for All Paths
     */
    private def findPath(start: T, allPaths: Boolean = false): Unit = {

        val priorityQueue = PriorityQueue[PathNode[T]]()
        distFromStart.clear()
        predecessors.clear()
        visited.clear()
        distFromStart.put(start, 0)
        predecessors.put(start, ArrayBuffer())
        iterations = 0

        priorityQueue.add(PathNode(start))
        // while the priority Q has elements, get the top one (least cost as per Comparator)
        while (!priorityQueue.isEmpty) {
            val pathNode = priorityQueue.poll()
            val (curNode, curDistance) = (pathNode._1.asInstanceOf[T], pathNode._2)
            boundary:
                log.trace(s"> connected nodes for node $curNode")
                for connectedNode <- g.getConnected(curNode).filter( _._1 != start) do {
                    val nextPathNode = PathNode(connectedNode._1, connectedNode._2)
                    // visited is used only when the first min path is needed
                    if allPaths || !visited.contains(nextPathNode) then {
                        iterations += 1
                        visited += nextPathNode
                        val totalDistance = curDistance + connectedNode._2
                        if totalDistance < distFromStart.getOrElse(connectedNode._1, Int.MaxValue) then {
                            distFromStart(connectedNode._1) = totalDistance
                            predecessors(connectedNode._1) = ArrayBuffer(curNode)
                            priorityQueue.add(PathNode(connectedNode._1, totalDistance))
                        } else if allPaths then
                            // if all paths are needed then add a predecessor to the existing list (can be very slow in big graphs!!)
                            predecessors(connectedNode._1) = predecessors.getOrElse(connectedNode._1, ArrayBuffer()) :+ curNode
                    }
                }
        }
    }

    private def getPaths(start: T, isAtEnd: T => Boolean, minPath: Boolean = false): Vector[Vector[(T, Int)]] = {
        val endStatuses = predecessors.keys.filter(isAtEnd(_))
        if endStatuses.isEmpty then return Vector()
        val allPaths = ArrayBuffer[ArrayBuffer[(T, Int)]]()
        val queue = util.ArrayDeque[ArrayBuffer[(T, Int)]]()
        var curPath = ArrayBuffer[(T, Int)]()
        for end <- endStatuses do
            queue.add(ArrayBuffer[(T, Int)]((end, distFromStart(end))))
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