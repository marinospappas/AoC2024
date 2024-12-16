package org.mpdev.scala.aoc2024
package solutions.day16

import framework.{AocMain, InputReader, PuzzleSolver}
import solutions.day16.ReindeerMinRoute.*
import utils.SimpleGrid.Direction.*
import utils.SimpleGrid.Direction
import utils.{+, Bfs, Djikstra, DjikstraV0, Graph, GraphBuilder, MinCostPath, SimpleGrid, adjacentCardinal, manhattan, *}

import scala.collection.mutable
import scala.math.abs
import scala.util.boundary
import scala.util.boundary.break

class ReindeerMinRoute(var testData: Vector[String] = Vector()) extends PuzzleSolver {

    val grid: SimpleGrid = SimpleGrid(if (testData.nonEmpty) testData else InputReader.read(16))
    val startState: State = (grid.findFirst(START), E)
    private val endPosition = grid.findFirst(END)
    val graph: Graph[State] = GraphBuilder[State]().withCustomGetConnected(state => getNeighbours(grid, state)).build()

    var allPaths: Vector[Vector[(State, Int)]] = Vector()

    override def part1: Any = {
        allPaths = Djikstra[State](graph).allPaths(startState, endState => endState._1 == endPosition)
        allPaths(0)(0)._2
    }

    override def part2: Any = {
        allPaths
            .flatMap(
                _.map(_._1).sliding(2).toVector
                    .flatMap(v => backTrack(v.head, v.last))
                    .distinct
            )
            .distinct
            .size
    }
}

object ReindeerMinRoute {
    val WALL = '#'
    val PATH = '.'
    val START = 'S'
    val END = 'E'
    val TURN_90_SCORE = 1000

    type State = ((Int,Int), Direction)

    def backTrack(state1: State, state2: State): Vector[(Int, Int)] = {
        val (start, dir) = state1
        val end = state2._1
        val n = if Set(N, S).contains(dir) then abs(start._2 - end._2) else abs(start._1 - end._1)
        (for i <- 0 to n yield start + dir.reverse.incr * i).toVector
    }
    
    def getNeighbours(grid: SimpleGrid, state: State): Set[(State, Int)] = {
        val (curPos, curDir) = state
        if grid.getDataPoint(curPos) == END then
            return Set()
        val states: Set[State] = Set(curDir, curDir.turnLeft, curDir.turnRight).map( newDir =>
            (findCrossingOrEndOfPath(grid, curPos, newDir), newDir)
        ).filter( _._1 != curPos )
        val result = states.map( s =>
            val dist = s._1.manhattan(curPos)
            (s, if s._2 == curDir then dist else dist + TURN_90_SCORE)
        )
        result
    }

    private def findCrossingOrEndOfPath(grid: SimpleGrid, position: (Int, Int), direction: Direction): (Int, Int) = {
        var curPos = position
        boundary:
            while grid.getDataPoint(curPos + direction.incr) != WALL do {
                curPos += direction.incr
                if curPos.adjacentCardinal.count(grid.getDataPointOrNull(_) == PATH) > 2 then
                    break()
            }
        curPos
    }
}
