package org.mpdev.scala.aoc2024
package utils

import java.util
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Bfs[T](g: Graph[T]) {
    
    def shortestPath(from: T, isAtEnd: T => Boolean): List[T] = {
        var curPath = ArrayBuffer[T](from)
        val visited = mutable.Set[T](from)
        val queue = util.ArrayDeque[ArrayBuffer[T]]()
        queue.add(curPath)
        while (!queue.isEmpty) {
            curPath = queue.poll()
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

    def traverseGraph (start: T, f: T => Unit): Unit = {
        val queue = util.ArrayDeque[T]()
        queue.add(start)
        val visited = mutable.Set[T](start)
        while !queue.isEmpty do {
            val current = queue.removeFirst()
            f(current)
            g.getConnected(current).foreach ( connection =>
                if (!visited.contains(connection._1)) {
                    visited.add(connection._1)
                    queue.add(connection._1)
                }
            )
        }
    }
    
    def getAllConnectedNodes(from: T): Set[T] = {
        val visited = mutable.Set(from)
        val queue = util.ArrayDeque[T]()
        queue.add(from)
        while !queue.isEmpty do {
            val current = queue.removeFirst()
            g.getConnected(current).map( _._1 ).foreach( connection =>
                if !visited.contains(connection) then {
                    visited.add(connection)
                    queue.add(connection)
                }
            )
        }
        visited.toSet
    }
}