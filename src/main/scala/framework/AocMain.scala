package org.mpdev.scala.aoc2024
package framework

private def usage(): Unit =
    System.err.println("usage: AoCMain #day")
    System.exit(1)

object AocMain {
    var environment = "prod"
}

@main
def aoc2024(args: String*): Unit =
    if (args.isEmpty)
        usage()
    val day = args(0).toInt
    val solver = solvers(day)
    println(s"\nSolving AoC 2015 day $day")
    val solution1 = solver.part1
    val solution2 = solver.part2
    println(s"  Part 1: ${solution1(0)}   in ${solution1(1)} msecs")
    println(s"  Part 2: ${solution2(0)}   in ${solution2(1)} msecs")
