package org.mpdev.scala.aoc2024
package solutions.day25

import framework.{InputReader, PuzzleSolver}
import utils.SimpleGrid
import solutions.day25.LockDecoder.LOCK_HEIGHT

import scala.collection.mutable

class LockDecoder extends PuzzleSolver {

    val inputData: Vector[String] = InputReader.read(25)
    private val numColumns = inputData(0).length
    private val grids: Vector[SimpleGrid] = inputData.sliding(8, 8).map(_.take(7)).map(SimpleGrid(_)).toVector
    val locks: Vector[SimpleGrid] = grids.filter( g => g.getRow(0).forall( _ == '.' ))
    val keys: Vector[SimpleGrid] = grids.filter( g => g.getRow(0).forall( _ == '#' ))
    val lockCombinations: Vector[Vector[Int]] = locks.map(g => (0 until numColumns).map(x => g.getColumn(x).count( _ == '#' )).toVector )
    val keyCombinations: Vector[Vector[Int]] = keys.map(g => (0 until numColumns).map(x => g.getColumn(x).count( _ == '#' )).toVector )

    private def keyFits(keyCombi: Vector[Int], lockCombi: Vector[Int]): Boolean =
        (0 until numColumns).forall( i => keyCombi(i) + lockCombi(i) <= LOCK_HEIGHT)

    override def part1: Any = {
        (for key <- keyCombinations yield
            for lock <- lockCombinations if keyFits(key, lock)
                yield (key, lock)).flatten.size
    }

    override def part2: Any =
        "End of AoC 2024 - Merry Christmas"
}

object LockDecoder {
    val LOCK_HEIGHT = 7
}
