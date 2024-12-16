package org.mpdev.scala.aoc2024
package utils

import scala.annotation.targetName
import scala.math.abs

extension (pair: (Int, Int))
    @targetName("plus")
    def +(other: (Int, Int)): (Int, Int) =
        (pair._1 + other._1, pair._2 + other._2)

    @targetName("minus")
    def -(other: (Int, Int)): (Int, Int) =
        (pair._1 - other._1, pair._2 - other._2)

    @targetName("times")
    def *(n: Int): (Int, Int) =
        (pair._1 * n, pair._2 * n)

    def manhattan(other: (Int, Int)): Int =
        abs(pair._1 - other._1) + abs(pair._2 - other._2)
    
    def x: Int = pair._1

    def y: Int = pair._2
