package org.mpdev.scala.aoc2024
package solutions.day21

import framework.{InputReader, PuzzleSolver}
import solutions.day21.RoboticArmController.{ENTER, dirKeypad, directionsForKeypad, directionsForKeypadLength, fromKeyToKey, numKeyPad}
import utils.SimpleGrid.Direction
import utils.{+, *}
import utils.SimpleGrid.Direction.W

import scala.collection.mutable
import scala.math.abs
import scala.util.boundary
import scala.util.boundary.break

class RoboticArmController extends PuzzleSolver {

    val keypadCodes: Vector[String] = InputReader.read(21)
    val numKeysGrid = SimpleGrid(numKeyPad)
    val dirKeysGrid = SimpleGrid(dirKeypad)

    // converts each code to movements for the human after the 2nd robot keypad
    def transform1(s: String): String =
        s.toDirectionsFromNumKeypad.toDirectionsFromDirKeypad.toDirectionsFromDirKeypad

    override def part1: Any =
        keypadCodes.map( code =>
            code.substring(0,3).toInt * transform1(code).length
        ).sum

    // converts each move (from-key, to-key) to number of keystrokes after n directional keypads
    def transform2(code: String, n: Int): Long =
        directionsForKeypadLength(dirKeysGrid, code.toDirectionsFromNumKeypad, n, 1)

    override def part2: Any = {
        RoboticArmController.cache.clear()
        keypadCodes.map(code =>
            code.substring(0, 3).toInt * transform2(code, 26)
        ).sum
    }

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
    val cache: mutable.Map[(Char, Char, Int), Long] = mutable.Map()

    def directionsForKeypad(kpd: SimpleGrid, sequence: String): String =
        sequence.sliding(2)
            .map(pair => fromKeyToKey(kpd, pair.head, pair.last))
            .mkString

    private def directionsForKeypadLength(kpd: SimpleGrid, sequence: String, n: Int, current: Int): Long = {
        if current == n then
            sequence.length
        else {
            val pairs = (ENTER + sequence).sliding(2).toList
            pairs.map( pair => if cache.contains((pair.head, pair.last, current))
                then cache((pair.head, pair.last, current))
                else {
                    val seq = fromKeyToKey(kpd, pair.head, pair.last)
                    val len = directionsForKeypadLength(kpd, seq, n, current + 1)
                    cache((pair.head, pair.last, current)) = len
                    len
                }
            ).sum
        }
    }

    private def isValidMovement(kpd: SimpleGrid, start: (Int, Int), directions: List[Char]): Boolean = {
        var current = start
        boundary:
            for d <- directions do {
                current += Direction.fromArrow(d).incr
                if kpd.getDataPoint(current) == NA then
                    break(false)
            }
            true
    }

    def fromKeyToKey(kpd: SimpleGrid, k1: Char, k2: Char): String = {
        val (fromCol, fromRow) = kpd.findFirst(k1)
        val (toCol, toRow) = kpd.findFirst(k2)
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
        if isValidMovement(kpd, (fromCol, fromRow), listPriority1) then listPriority1.mkString + ENTER
        else listPriority2.mkString + ENTER
    }
}
