package org.mpdev.scala.aoc2024
package utils

import java.util
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Bfs[T](g: Graph[T]) {
    
    def shortestPathBfs(from: T, isAtEnd: T => Boolean): List[T] = {
        var curPath = ArrayBuffer[T](from)
        val visited = mutable.Set[T](from)
        val queue = util.ArrayDeque[ArrayBuffer[T]]()
        queue.add(curPath)
        while (!queue.isEmpty) {
            curPath = queue.removeFirst()
            val lastNode = curPath(curPath.size - 1)
            if isAtEnd(lastNode) then   // found path
                return curPath.toList
            g.getConnected(lastNode).map ( _._1 ).foreach ( connection =>
                if !curPath.contains(connection) && !visited.contains(connection) then {
                    visited.add(connection)
                    val newPartialPath = curPath.to(ArrayBuffer)
                    newPartialPath += connection
                    queue.add(newPartialPath)
                }
            )
        }
        List()
    }

    def allPaths(from: T, isAtEnd: T => Boolean): List[List[T]] = {
        val allPaths = ArrayBuffer[ArrayBuffer[T]]()
        val queue = util.ArrayDeque[ArrayBuffer[T]]()
        var curPath = ArrayBuffer[T](from)
        queue.add(curPath)
        while !queue.isEmpty do {
            curPath = queue.removeFirst()
            val lastNode = curPath(curPath.size - 1)
            if isAtEnd(lastNode) then   // found path
                allPaths += curPath
            else
                g.getConnected(lastNode).map ( _._1 ).foreach ( connectedNode =>
                    if !curPath.contains(connectedNode) then {
                        val newPartialPath = curPath.to(ArrayBuffer)
                        newPartialPath += connectedNode
                        queue.add(newPartialPath)
                    }
                )
        }
        allPaths.map(_.toList).toList
    }

    /*def getAllConnectedNodes(from: T): Set<T> {
        val visited = mutableSetOf(from)
        val queue = ArrayDeque<T>().also { l -> l.add(from) }
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            getConnected(current).map { it.first }.sortedBy { it }.forEach { connection ->
                if (!visited.contains(connection)) {
                    visited.add(connection)
                    queue.add(connection)
                }
            }
        }
        return visited
    }*/
}