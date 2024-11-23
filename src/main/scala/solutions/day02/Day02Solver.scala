package org.mpdev.scala.aoc2024
package solutions.day02

import framework.{InputReader, PuzzleSolver}

class Day02Solver extends PuzzleSolver {

    val inputData: List[Parcel] = InputReader.read(2).map( s => parcel"$s" )  //InputReader.read(2).transform

    override def solvePart1: Any =
        inputData.map(p => p.area() + p.extra()).sum

    override def solvePart2: Any =
        inputData.map(_.ribbon()).sum

    extension (sc: StringContext)
        private def parcel(args: String*): Parcel = {
            val d = sc.s(args*).split("x", 3).map { _.toIntOption.getOrElse(0) }
            Parcel(d(0), d(1), d(2))
        }
}
