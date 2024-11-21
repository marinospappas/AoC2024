package org.mpdev.scala.aoc2015
package utils

import org.mpdev.scala.aoc2024.utils.Graph

import scala.collection.mutable

class Dfs[T](g: Graph[T]) {

    /////////////////////////////////////////

    private var minPath: Int = Int.MaxValue
    private val cache: mutable.Map[T, Int] = mutable.Map()
    private var iterations: Int = 0

    def shortestPath(start: T, end: T): (Int, Int) =
        minPath = Int.MaxValue
        iterations = 0
        findMinPath(start, end, 0, mutable.Map())
        (minPath, iterations)

    private def findMinPath(curr: T, to: T, pathLength: Int, visited: mutable.Map[T, Int]): Int =
        iterations += 1
        if (curr == to) {
            if (pathLength < minPath)
                minPath = pathLength
            return minPath
        }
        g.getConnected(curr).foreach((neighbor, distance) =>
            if (!visited.contains(curr))
                visited += neighbor -> findMinPath(neighbor, to, pathLength + distance, visited)
        )
        minPath

    ///////////////////////////////////////

    def longestPath(start: T, includeAllNodes: Boolean = true, isAtEnd: T => Boolean): Int =
        findMaxPath(start, isAtEnd, mutable.Map(), includeAllNodes)

    //TODO: refactor the below function to use Stack instead of recursion
    private def findMaxPath(cur: T, isAtEnd: T => Boolean, visited: mutable.Map[T, Int], includeAllNodes: Boolean): Int =
        if (isAtEnd(cur) && (if (includeAllNodes) visited.size == g.nodes.size else visited.nonEmpty))
            return visited.values.sum()

        var maxPath = Int.MinValue
        g.getConnected(cur).foreach((neighbor, steps) =>
            if (!visited.contains(neighbor))
                visited += neighbor -> steps
                val res = findMaxPath(neighbor, isAtEnd, visited, includeAllNodes)
                if (res > maxPath)
                    maxPath = res
                visited.remove(neighbor)
        )
        maxPath

}
