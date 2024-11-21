package org.mpdev.scala.aoc2024
package utils

class MinCostPath[T] {

    var path: List[(T,Int)] = List()
    var minCost: Int = Int.MaxValue
    var numberOfIterations: Int = 0

    def printPath(): Unit =
        println(s"path,cost: $path")
        println(s"min cost: $minCost")
    
}
