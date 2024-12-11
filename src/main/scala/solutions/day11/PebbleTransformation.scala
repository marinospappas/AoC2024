package org.mpdev.scala.aoc2024
package solutions.day11

import framework.{AocMain, InputReader, PuzzleSolver}
import solutions.day11.PebbleTransformation.Stone.{EVEN_DIGITS, ODD_DIGITS, START, ZERO}
import solutions.day11.PebbleTransformation.Stone

import scala.collection.{immutable, mutable}
import scala.collection.mutable.{ArrayBuffer, Map}

class PebbleTransformation extends PuzzleSolver {

    val inputData: List[Stone] = InputReader.read(11).head.split(" ").toList.map( Stone.fromString )
    private val cache = mutable.Map[Stone, Long]()

    def getNextStones(stone: Stone): List[Stone] = {
        stone match
            case START => inputData
            case ZERO => List(Stone.ODD_DIGITS("1"))
            case EVEN_DIGITS(number) => number.splitAt(number.length / 2).toList.map(_.toLong.toString).map(Stone.fromString)
            case ODD_DIGITS(number) => List(Stone.fromString((number.toLong * 2024).toString))
    }

    def transformWithCache(stones: List[Stone], n: Int): Long = {
        var count = 0
        var cache = stones.map(stone => stone -> 1L).toMap
        while { count += 1
            count <= n
        } do {
            val newCache = mutable.Map[Stone, Long]()
            cache.foreach( (stone, curValue) =>
                for newStone <- getNextStones(stone) do
                    newCache.put(newStone, newCache.getOrElse(newStone, 0L) + curValue)
            )
            cache = immutable.Map.from(newCache)
        }
        cache.values.sum
    }

    override def part1: Any =
        transformWithCache(inputData, 25)

    override def part2: Any =
        transformWithCache(inputData, 75)

}

object PebbleTransformation {
    enum Stone {
        case START
        case ZERO
        case EVEN_DIGITS(num: String)
        case ODD_DIGITS(num: String)
    }
    object Stone {
        def fromString(s: String): Stone = {
            if s == "0" then ZERO
            else if s.length % 2 == 0 then EVEN_DIGITS(s)
            else ODD_DIGITS(s)
        }
    }
}
