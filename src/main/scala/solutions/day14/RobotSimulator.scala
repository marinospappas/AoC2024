package org.mpdev.scala.aoc2024
package solutions.day14

import framework.{AocMain, InputReader, PuzzleSolver}
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
        var grid = GridBuilder[Char]().withMapper(Map('X' -> 'X'))
            .fromPointsArray(robots.map(r => Point(r.initPos.x, r.initPos.y)).toArray).build()
        if AocMain.environment == "test" then println(grid)
        boundary:
            for i <- 1 to 1_000_000 do {
                if i % 1000 == 0 then print(".")
                grid = GridBuilder[Char]().withMapper(Map('X' -> 'X'))
                    .fromPointsArray(robots.map(r => Point(r.curPos(i).x, r.curPos(i).y)).toArray).build()
                val maxRobotConcentratedPts = grid.findAllAreas.map( _._2 ).maxBy( _.size )
                if maxRobotConcentratedPts.size > robots.size / 3 then {
                    println()
                    println(grid)
                    println(s"Number of robots in Xmas tree: ${maxRobotConcentratedPts.size}")
                    break(i)
                }
            }
            -1
    }
}

object RobotSimulator {

    var MAX_X = 0
    var MAX_Y = 0

    def getQuarter(point: (Int, Int)): Int = {
        val midX = MAX_X / 2
        val midY = MAX_Y / 2
        point match {
            case p if p.x < midX && p.y < midY => 0
            case p if p.x > midX && p.y < midY => 1
            case p if p.x < midX && p.y > midY => 2
            case p if p.x > midX && p.y > midY => 3
            case _ => -1
        }
    }

    // input parsing
    private def toRobot(s: String): Robot =
        s match { case s"p=${p1},${p2} v=${v1},${v2}" => Robot((p1.toInt, p2.toInt), (v1.toInt, v2.toInt)) }

}
