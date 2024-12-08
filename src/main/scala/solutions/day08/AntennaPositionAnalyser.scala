package org.mpdev.scala.aoc2024
package solutions.day08

import framework.{InputReader, PuzzleSolver}
import utils.{-, +, SimpleGrid, *}
import solutions.day08.AntennaPositionAnalyser.*

class AntennaPositionAnalyser extends PuzzleSolver {

    val grid: SimpleGrid = SimpleGrid(InputReader.read(8))
    var antennas: Map[Char, Set[(Int, Int)]] = grid.getDataValues.filter( _ != EMPTY ).map( c => (c, grid.findAll(c)) ).toMap

    def findFirstAntinodes(pair: List[(Int, Int)]): Set[(Int, Int)] = {
        val antennaDistance = pair(1) - pair.head
        Set(pair(1) + antennaDistance, pair.head - antennaDistance).filter(grid.isInsideGrid)
    }

    def findAllAntinodes(pair: List[(Int, Int)]): Set[(Int, Int)] = {
        val (ant1, ant2) = (pair.head, pair(1))
        val antennaDistance = ant2 - ant1
        Set(1, -1).flatMap( dir => Iterator.iterate(ant2)( _ + antennaDistance * dir ).takeWhile( grid.isInsideGrid ).toSet )
    }

    def toAntinodes(pair: List[(Int, Int)], part1: Boolean): Set[(Int, Int)] =
        if part1 then findFirstAntinodes(pair) else findAllAntinodes(pair)

    def findAntinodes(part1: Boolean): Set[(Int, Int)] =
        (for {
            position <- antennas.values
            a1 <- position
            a2 <- position
            if a1 != a2
            antiNode <- toAntinodes(List(a1, a2), part1)
        } yield antiNode).toSet
// alternatively can be done with combinations
//        antennas.map( _._2.toList.combinations(2) )           // List of Antennas / List of Positions / Combinations of 2
//            .flatMap( _.map( toAntinodes(_, part1)) )         // List of Antennas / List of Antinodes positions
//            .flatten.toSet                                    // Set of Unique Antinodes positions

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