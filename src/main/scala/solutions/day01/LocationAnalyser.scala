package org.mpdev.scala.aoc2024
package solutions.day01

import framework.{AoCException, InputReader, PuzzleSolver}
import scala.collection.mutable.ArrayBuffer
import scala.math.abs

class LocationAnalyser extends PuzzleSolver {

    val (listA, listB) = {
        val (l1, l2) = (ArrayBuffer[Int](), ArrayBuffer[Int]())
        InputReader.read(1).map(s => toPair"$s").foreach(pair => { l1 += pair._1; l2 += pair._2 })
        (l1.toList, l2.toList)
    }
    
    override def part1: Any = {
        val (l1, l2) = (listA.sorted, listB.sorted)
        l1.indices.map( i => abs(l1(i) - l2(i)) ).sum
    }

    override def part2: Any =
        listA.map( a => a * listB.count( b => b == a ) ).sum

    extension (sc: StringContext)
        private def toPair(args: String*): (Int, Int) = {
            sc.s(args *) match
                case s"${l1}   ${l2}" => (l1.toInt, l2.toInt)
                case other => throw AoCException(s"bad input: $other")
        }
}
