package org.mpdev.scala.aoc2024
package framework

import AocMain.{time, usage}

object AocMain {
    var environment = "prod"
    def usage(): Unit = {
        System.err.println("usage: AoCMain #day")
        System.exit(1)
    }
    def time(block: => Any): (Any, Long) = {
        val start = System.currentTimeMillis()
        (block, System.currentTimeMillis() - start)
    }
}

@main
def aoc2024(args: String*): Unit = {
    if (args.isEmpty)
        usage()
    val day = args(0).toIntOption.getOrElse(throw AoCException(s"bad argument for day: ${args(0)}"))
    if (!solvers.contains(day))
        throw AoCException(s"Solver for Day $day Not Configured")
    val solver = solvers(day)
    println(s"\nSolving AoC 2024 day $day")
    val solution1 = time(solver.part1)
    val solution2 = time(solver.part2)
    println(s"  Part 1: ${solution1(0)}   in ${solution1(1)} msecs")
    println(s"  Part 2: ${solution2(0)}   in ${solution2(1)} msecs")
}
