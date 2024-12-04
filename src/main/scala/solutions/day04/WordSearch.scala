package org.mpdev.scala.aoc2024
package solutions.day04

import framework.{InputReader, PuzzleSolver}

import utils.{Grid, GridBuilder, GridUtils, Point}
import utils.GridUtils.Direction.*

class WordSearch extends PuzzleSolver {

    val grid: Grid[Char] = GridBuilder[Char]().withMapper(Grid.allCharsDefMapper).fromVisualGrid(InputReader.read(4)).build()
    private val searchWord = "XMAS"
    private val (startOfSearchPart1, startOfSearchPart2) = (searchWord.head, 'A')

    def matchesWord(point: Point, direction: GridUtils.Direction): Boolean = {
        val checkWord = searchWord.indices.map( i => grid.getDataPointOrNull(point + direction.increment * i) ).mkString
        searchWord == checkWord
    }

    def countWordMatches(p: Point): Int = {
        if grid.getDataPoint(p) != startOfSearchPart1 then 0
        else GridUtils.Direction.allDirections.count( matchesWord(p, _) )
    }

    private def matchesXMas(p: Point): Boolean = {
        grid.getDataPoint(p) == startOfSearchPart2 &&
            Set("MMSS", "SMMS", "SSMM", "MSSM")
                .contains(List(NW, NE, SE, SW).map(dir => grid.getDataPointOrNull(p + dir.increment)).mkString)
    }

    override def part1: Any =
        grid.getDataPoints.map( (p, _) => countWordMatches(p) ).sum

    override def part2: Any =
        grid.getDataPoints.count( (p, _) => matchesXMas(p) )
}
