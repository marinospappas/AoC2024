package org.mpdev.scala.aoc2024
package solutions.day14

import framework.{InputReader, PuzzleSolver}
import utils.*
import solutions.day14.RobotSimulator.{MAX_X, MAX_Y, getQuarter, toRobot}

import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import scala.util.boundary.break

class RobotSimulator extends PuzzleSolver {

    val robots: List[Robot] = InputReader.read(14).map( toRobot )
    MAX_X = robots.map( _.initPos.x ).max
    MAX_Y = robots.map( _.initPos.y ).max

    override def part1: Any =
        robots.map( _.curPos(100) ).map( getQuarter ).filter(_ >= 0).groupBy(identity).values.map(_.size)
            .foldLeft(1L)((prod, cur) => prod * cur.toLong)

    override def part2: Any = {
        var grid = GridBuilder[Char]()
            .withMapper(Map('X' -> 'X'))
            .fromPointsArray(robots.map(r => Point(r.initPos.x, r.initPos.y)).toArray)
            .build()
        println(grid)
        boundary:
            for i <- 1 to 1_000_000 do {
                if i % 1000 == 0 then print(".")
                grid = GridBuilder[Char]()
                    .withMapper(Map('X' -> 'X'))
                    .fromPointsArray(robots.map(r => Point(r.curPos(i).x, r.curPos(i).y)).toArray)
                    .build()
                val maxRobotConcentratedPts = grid.findAllAreas.map( _._2 ).maxBy( _.size )
                if maxRobotConcentratedPts.size > robots.size / 4 then {
                    println()
                    println(grid)
                    println(s"number of robots in tree: ${maxRobotConcentratedPts.size}")
                    break(i)
                }
            }
            -1
    }
}

object RobotSimulator {

    var MAX_X = 0
    var MAX_Y = 0

    def getQuarter(p: (Int, Int)): Int = {
        val midX = MAX_X / 2
        val midY = MAX_Y / 2
        if p.y < midY then {
            if p.x < midX then 0 else if p.x > midX then 1 else -1
        } else if p.y > midY then {
            if p.x < midX then 2 else if p.x > midX then 3 else -1
        } else -1
    }

    // input parsing
    private def toRobot(s: String): Robot =
        s match { case s"p=${p1},${p2} v=${v1},${v2}" => Robot((p1.toInt, p2.toInt), (v1.toInt, v2.toInt)) }

}
