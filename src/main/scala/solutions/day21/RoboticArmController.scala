package org.mpdev.scala.aoc2024
package solutions.day21

import framework.{InputReader, PuzzleSolver}
import solutions.day21.RoboticArmController.{ENTER, dirKeypad, directionsForKeypad, fromKeyToKey, keyToKeyAllMovements, numKeyPad}
import utils.SimpleGrid.Direction
import utils.{+, *}

import scala.collection.mutable.ArrayBuffer
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

    def numToNumMinMovements(n1: Char, n2: Char): String = {
        val combis1 = keyToKeyAllMovements(numKeysGrid, n1, n2).map(_ + ENTER)
        println(combis1)
        val combisX2 = combis1.map(ENTER + _).map(s => s.sliding(2).map(pair =>
            keyToKeyAllMovements(dirKeysGrid, pair.head, pair.last).map(_ + ENTER)
        ))
        val combis2 = ArrayBuffer[String]()
        combisX2.map(_.toList).foreach(list =>
            combis2 ++= list.head
            val newList = ArrayBuffer[String]()
            var curList = List[String]() ++ combis2
            for i <- 1 until list.length do {
                curList.foreach(s1 =>
                    list(i).foreach(s2 =>
                        newList += (s1 + s2)
                    )
                )
                println(newList)
                combis2.clear()
                combis2 ++= newList
                curList = List[String]() ++ combis2
            }
        )
        //println(combis2)
        val combis3 = combis2
        combis3.toString()
    }

    override def part1: Any =
        keypadCodes.map( code =>
            code.substring(0,3).toInt * code.toDirectionsFromNumKeypad.toDirectionsFromDirKeypad.toDirectionsFromDirKeypad.length
        ).sum

    def transform(s: String): String =
        s.toDirectionsFromNumKeypad.toDirectionsFromDirKeypad.toDirectionsFromDirKeypad

    override def part2: Any =
        0

    extension(s: String) {
        private def toDirectionsFromNumKeypad: String =
            println(s"input: $s")
            val output = directionsForKeypad(numKeysGrid, ENTER + s)
            println(s"output: $output")
            output

        private def toDirectionsFromDirKeypad: String =
            println(s"input: $s")
            val output = directionsForKeypad(dirKeysGrid, ENTER + s)
            println (s"output: $output")
            output
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

    def keyToKeyAllMovements(grid: SimpleGrid, k1: Char, k2: Char): Vector[String] = {
        val (fromCol, fromRow) = grid.findFirst(k1)
        val (toCol, toRow) = grid.findFirst(k2)
        val (countHor, countVert) = (abs(toCol - fromCol), abs(toRow - fromRow))
        val moveX = if countHor == 0 then 0 else (toCol - fromCol) / countHor
        val moveY = if countVert == 0 then countVert else (toRow - fromRow) / countVert
        (List.fill(countHor)(Direction.fromIncr(moveX, 0)) ++ List.fill(countVert)(Direction.fromIncr(0, moveY)))
            .map(_.symbol).permutations.map(_.mkString)
            .filter(s => isValidMovement(grid, (fromCol, fromRow), s.toList))
            .toVector
    }

    def isValidMovement(grid: SimpleGrid, start: (Int, Int), directions: List[Char]): Boolean = {
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
        val list1 = List.fill(countHor)(Direction.fromIncr(moveX, 0).symbol) ++
            List.fill(countVert)(Direction.fromIncr(0, moveY).symbol)
        val list2 = List.fill(countVert)(Direction.fromIncr(0, moveY).symbol) ++
            List.fill(countHor)(Direction.fromIncr(moveX, 0).symbol)
        if isValidMovement(grid, (fromCol, fromRow), list1) then list1.mkString + ENTER
        else list2.mkString + ENTER
    }

    def fromDirToKey(grid: SimpleGrid, start: Char, directions: String): String = {
        var curPos = grid.findFirst(start)
        directions.split(ENTER).map(s =>
            s.foreach(c => curPos += Direction.fromArrow(c).incr)
            grid.getDataPoint(curPos)
        ).mkString
    }
}
