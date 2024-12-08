package org.mpdev.scala.aoc2024
package solutions.day08

import framework.{InputReader, PuzzleSolver}
import utils.SimpleGrid
import solutions.day08.AntennaPositionAnalyser.*

import scala.collection.mutable

class AntennaPositionAnalyser extends PuzzleSolver {

    val grid: SimpleGrid = SimpleGrid(InputReader.read(8))
    var antennas: Map[Char, Set[(Int, Int)]] = ANTENNAS.map(c => (c, grid.findAll(c)) ).filter( _._2.nonEmpty ).toMap
    var antennasPositions: Set[(Int, Int)] = antennas.values.flatten.toSet
    
    def findFirstAntinodes(pair: List[(Int, Int)]): Set[(Int, Int)] = {
        val (x1, y1, x2, y2) = (pair.head._1, pair.head._2, pair(1)._1, pair(1)._2)
        Set((2 * x2 - x1, 2 * y2 - y1), (2 * x1 - x2, 2 * y1 - y2)).filter(grid.isInsideGrid)
    }

    def findAllAntinodes(pair: List[(Int, Int)]): Set[(Int, Int)] = {
        val (x1, y1, x2, y2) = (pair.head._1, pair.head._2, pair(1)._1, pair(1)._2)
        var (n, position) = (0, (-1, -1))
        val result = mutable.Set[(Int, Int)]()
        for dir <- Set(-1, 1) do {
            n = 0
            while {
                position = (x2 + dir * n * (x2 - x1), y2 + dir * n * (y2 - y1))
                grid.isInsideGrid(position)
            } do {
                result += position
                n += 1
            }
        }
        result.toSet
    }

    def toAntinodes(pair: List[(Int, Int)], part1: Boolean): Set[(Int, Int)] = 
        if part1 then findFirstAntinodes(pair) else findAllAntinodes(pair)
    
    def findAntinodes(part1: Boolean): Set[(Int, Int)] =
        antennas.map(_._2.toList.combinations(2))           // List of Antennas / List of Positions / Combinations of 2
            .flatMap(_.map( toAntinodes(_, part1)))         // List of Antennas / List of Antinodes positions
            .flatten.toSet                                  // Set of Unique Antinodes positions

    override def part1: Any =
        findAntinodes(part1 = true).size

    override def part2: Any =
        findAntinodes(part1 = false).size
}

object AntennaPositionAnalyser {
    val EMPTY = '.'
    val ANTI_NODE = '#'
    private val ANTENNAS: Seq[Char] = '0' to 'z'
}