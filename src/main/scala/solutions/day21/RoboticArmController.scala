package org.mpdev.scala.aoc2024
package solutions.day21

import framework.{InputReader, PuzzleSolver}
import solutions.day21.RoboticArmController.{ENTER, dirKeypad, directionsForKeypad, fromKeyToKey, numKeyPad}
import utils.SimpleGrid.Direction
import utils.{+, *}
import utils.SimpleGrid.Direction.W

import scala.math.abs
import scala.util.boundary
import scala.util.boundary.break

class RoboticArmController extends PuzzleSolver {

    val keypadCodes: Vector[String] = InputReader.read(21)
    val numKeysGrid = SimpleGrid(numKeyPad)
    val dirKeysGrid = SimpleGrid(dirKeypad)

    def keySeqToDirections(s: String): String =
        (ENTER + s).sliding(2)
            .map( s => s.toDirectionsFromNumKeypad)
            .mkString

    override def part1: Any =
        keypadCodes.map( code =>
            code.substring(0,3).toInt * transform(code).length
        ).sum

    def transform(s: String): String =
        s.toDirectionsFromNumKeypad.toDirectionsFromDirKeypad.toDirectionsFromDirKeypad

    override def part2: Any =
        0

    extension(s: String) {
        private def toDirectionsFromNumKeypad: String =
            directionsForKeypad(numKeysGrid, ENTER + s)

        private def toDirectionsFromDirKeypad: String =
            directionsForKeypad(dirKeysGrid, ENTER + s)
    }
}

object RoboticArmController {

    val ENTER = 'A'
    val NA = ' '

    val numKeyPad: Vector[String] = Vector(
        "789",
        "456",
        "123",
        " 0A",
    )
    val dirKeypad: Vector[String] = Vector(
        " ^A",
        "<v>",
    )

    def directionsForKeypad(grid: SimpleGrid, sequence: String): String =
        sequence.sliding(2)
            .map(pair => fromKeyToKey(grid, pair.head, pair.last))
            .mkString

    private def isValidMovement(grid: SimpleGrid, start: (Int, Int), directions: List[Char]): Boolean = {
        var current = start
        boundary:
            for d <- directions do {
                current += Direction.fromArrow(d).incr
                if grid.getDataPoint(current) == NA then
                    break(false)
            }
            true
    }

    def fromKeyToKey(grid: SimpleGrid, k1: Char, k2: Char): String = {
        val (fromCol, fromRow) = grid.findFirst(k1)
        val (toCol, toRow) = grid.findFirst(k2)
        val (countHor, countVert) = (abs(toCol - fromCol), abs(toRow - fromRow))
        val moveX = if countHor == 0 then 0 else (toCol - fromCol) / countHor
        val moveY = if countVert == 0 then countVert else (toRow - fromRow) / countVert
        var curPos = (fromCol, fromRow)
        // horizontal moves and vertical moves are grouped together
        // as this leads to fewer moves in the next kbd
        // e.g. instead of >v> we do >>v
        val horizMoves = List.fill(countHor)(Direction.fromIncr(moveX, 0).symbol) ++
            List.fill(countVert)(Direction.fromIncr(0, moveY).symbol)
        val vertMoves = List.fill(countVert)(Direction.fromIncr(0, moveY).symbol) ++
            List.fill(countHor)(Direction.fromIncr(moveX, 0).symbol)
        // if the movement includes "<" then this will be the first priority
        // as it leads to fewer movements down the line due to the keyboard arrangement
        // otherwise vertical movement is prioritised over horizontal
        // this, of course provided the movement does not lead via the blank button
        val (listPriority1, listPriority2) = if Direction.fromIncr(moveX, 0) == W
            then (horizMoves, vertMoves)
            else (vertMoves, horizMoves)
        if isValidMovement(grid, (fromCol, fromRow), listPriority1) then listPriority1.mkString + ENTER
        else listPriority2.mkString + ENTER
    }
}
