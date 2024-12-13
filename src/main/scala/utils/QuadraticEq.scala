package org.mpdev.scala.aoc2024
package utils

import scala.math.sqrt

object QuadraticEq {

    def solve(a: Int, b: Int, c: Int): (Double, Double) =
        solveLong(a.toLong, b.toLong, c.toLong)

    def solveLong(a: Long, b: Long, c: Long): (Double, Double) = {
        // b squared - 4 a c
        val d = sqrt((b * b - 4 * a * c).toDouble)
        val x1 = (-b - d) / (2 * a)
        val x2 = (-b + d) / (2 * a)
        (x1, x2)
    }

    def solveDouble(a: Double, b: Double, c: Double): (Double, Double) = {
        // b squared - 4 a c
        val d = sqrt(b * b - 4 * a * c)
        val x1 = (-b - d) / (2 * a)
        val x2 = (-b + d) / (2 * a)
        (x1, x2)
    }
}