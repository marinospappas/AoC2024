package org.mpdev.scala.aoc2024
package utils

import scala.annotation.targetName
import scala.math.{ceil, floor}

object LinearEqSys {

    /**
     * solves:
     * a1 * x + b1 * y = c1
     * a2 * x + b2 * y = c2
     */
    def solve2(a: (Long, Long), b: (Long, Long), c: (Long, Long)): (Double,Double) = {
        val d = (b._1 * a._2 - a._1 * b._2).toDouble
        if d == 0.0 then return (Double.NaN, Double.NaN)
        val y = (c._1 * a._2 - a._1 * c._2) / d
        val x = (b._1 * c._2 - c._1 * b._2) / d
        (x, y)
    }

    def solve2Long(a: (Int, Int) | (Long, Long), b: (Int, Int) | (Long, Long), c: (Int, Int) | (Long, Long)): (Long, Long) | Null = {
        val d = (b._1 * a._2 - a._1 * b._2).toDouble
        if d == 0.0 then return null
        val y = (c._1 * a._2 - a._1 * c._2) / d
        val x = (b._1 * c._2 - c._1 * b._2) / d
        if ceil(x) != floor(x) || ceil(y) != floor(y) then return null
        (x.toLong, y.toLong)
    }

    extension (i: Int | Long)
        def toLong: Long =
            i match
                case i1: Int => i1.toLong
                case _ => i.asInstanceOf[Long]

        @targetName("times")
        def *(j: Int | Long): Long =
            i.toLong * j.toLong
}