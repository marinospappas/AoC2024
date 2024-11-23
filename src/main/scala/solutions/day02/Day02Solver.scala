package org.mpdev.scala.aoc2024
package solutions.day02

import framework.{AoCException, InputReader, PuzzleSolver}

class Day02Solver extends PuzzleSolver {

    val inputData: List[Parcel] = InputReader.read(2).map( s => parcel"$s" )  //InputReader.read(2).transform

    override def part1: Any =
        inputData.map(p => p.area() + p.extra()).sum

    override def part2: Any =
        inputData.map(_.ribbon()).sum

    extension (sc: StringContext)
        private def parcel(args: String*): Parcel = {
            sc.s(args *) match
                case s"${l}x${w}x$h" => Parcel(l.toInt, w.toInt, h.toInt)
                case other => throw AoCException(s"bad input: $other")
        }
}
