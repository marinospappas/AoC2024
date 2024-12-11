package org.mpdev.scala.aoc2024
package solutions.day11

import framework.{AocMain, InputReader, PuzzleSolver}
import solutions.day11.PebbleTransformation.Stone.{EVEN_DIGITS, ODD_DIGITS, START, ZERO, getLevel}
import solutions.day11.PebbleTransformation.Stone

import java.util
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, Map}
import scala.math.abs
import scala.util.boundary
import scala.util.boundary.break

class PebbleTransformation extends PuzzleSolver {

    val inputData: List[Stone] = InputReader.read(11).head.split(" ").toList.map( Stone.fromString(0, _) )
    private val cache = mutable.Map[String, List[String]]()

    def transformStonesSeq(stones: List[Stone]): List[Stone] = {
        val newStones = ArrayBuffer[Stone]()
        for stone <- stones do
            newStones ++= getNextStones(stone)
        if AocMain.environment != "prod" then println(newStones.length)
        newStones.toList
    }

    def transformAllMultiple(stones: List[Stone], n: Int): List[Stone] = {
        var newStones = stones
        for i <- 1 to n do
            newStones = transformStonesSeq(newStones)
        newStones
    }

    def getNextStones(stone: Stone): List[Stone] = {
        stone match
            case START => inputData
            case ZERO(level) => List(Stone.ODD_DIGITS(level + 1, "1"))
            case EVEN_DIGITS(level, number) => number.splitAt(number.length / 2).toList.map(_.toLong.toString).map(Stone.fromString(level + 1, _))
            case ODD_DIGITS(level, number) => List(Stone.fromString( level + 1, (number.toLong * 2024).toString))
    }

    // TODO: investigate and debug BFS solution
    def traverseStonesBfs(start: Stone, count: Int): Long = {
        val queue = util.ArrayDeque[Stone]()
        queue.add(start)
        var curPath = ArrayBuffer[Stone](start)
        val visited = mutable.Set[Stone](start)
        var level = getLevel(start)
        var totalLength = 0L
        while !queue.isEmpty && level < count do {
            val current = queue.removeFirst()
            val nextStones = getNextStones(current)
            totalLength += nextStones.size
            nextStones.foreach(connection =>
                level = getLevel(connection)
                if (!visited.contains(connection)) {
                    visited.add(connection)
                    queue.add(connection)
                }
            )
        }
        totalLength
    }

    override def part1: Any =
        transformAllMultiple(inputData, 25).length

    override def part2: Any =
        traverseStonesBfs(START, 75)

}

object PebbleTransformation {
    enum Stone {
        case START
        case ZERO(level: Int)
        case EVEN_DIGITS(level: Int, num: String)
        case ODD_DIGITS(level: Int, num: String)
    }
    object Stone {
        def fromString(level: Int, s: String): Stone = {
            if s == "0" then ZERO(level)
            else if s.length % 2 == 0 then EVEN_DIGITS(level, s)
            else ODD_DIGITS(level, s)
        }
        def getLevel(stone: Stone): Int =
            stone match
                case START => 0
                case ZERO(level) => level
                case EVEN_DIGITS(level, _) => level
                case ODD_DIGITS(level, _) => level
    }
}
