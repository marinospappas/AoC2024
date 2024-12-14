package org.mpdev.scala.aoc2024
package solutions.day11

import framework.{AocMain, InputReader, PuzzleSolver}
import solutions.day11.PebbleTransformation.Stone.{EVEN_DIGITS, ODD_DIGITS, ZERO}
import solutions.day11.PebbleTransformation.Stone

import scala.collection.{immutable, mutable}
import scala.collection.mutable.ArrayBuffer

class PebbleTransformation extends PuzzleSolver {

    val inputData: List[Stone] = InputReader.read(11).head.split(" ").toList.map( Stone.fromString )

    def getNextStones(stone: Stone): List[Stone] = {
        stone match
            case ZERO => List(Stone.ODD_DIGITS("1"))
            case EVEN_DIGITS(number) => number.splitAt(number.length / 2).toList.map(_.toLong.toString).map(Stone.fromString)
            case ODD_DIGITS(number) => List(Stone.fromString((number.toLong * 2024).toString))
    }

    def transformWithDictionary(stones: List[Stone], n: Int): Map[Stone, Long] = {
        var count = 0
        var dictionary = stones.map(stone => stone -> 1L).toMap
        while { count += 1
            count <= n
        } do {
            val newDictionary = mutable.Map[Stone, Long]()
            dictionary.foreach( (stone, curValue) =>
                for newStone <- getNextStones(stone) do
                    newDictionary.put(newStone, newDictionary.getOrElse(newStone, 0L) + curValue)
            )
            dictionary = immutable.Map.from(newDictionary)
        }
        if AocMain.environment != "prod" then dictionary.foreach(println)
        dictionary
    }

    override def part1: Any =
        transformWithDictionary(inputData, 25).values.sum

    override def part2: Any =
        transformWithDictionary(inputData, 75).values.sum

}

object PebbleTransformation {
    enum Stone {
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
