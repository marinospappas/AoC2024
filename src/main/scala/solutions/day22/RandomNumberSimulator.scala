package org.mpdev.scala.aoc2024
package solutions.day22

import framework.{InputReader, PuzzleSolver}
import solutions.day22.RandomNumberSimulator.{mix, prune}

import scala.collection.mutable.ArrayBuffer

class RandomNumberSimulator extends PuzzleSolver {

    val inputData: Vector[Int] = InputReader.read(22).map( _.toInt )

    // Calculate the result of multiplying the secret number by 64.
    // Then, mix this result into the secret number. Finally, prune the secret number.
    // Calculate the result of dividing the secret number by 32.
    // Round the result down to the nearest integer.
    // Then, mix this result into the secret number. Finally, prune the secret number.
    // Calculate the result of multiplying the secret number by 2048.
    // Then, mix this result into the secret number. Finally, prune the secret number.
    def generateNextNumber(seed: Long): Long = {
        var res = seed << 6    // multiply by 64
        var secret = seed.mix(res).prune
        res = secret >>> 5   // divide by 32
        secret = secret.mix(res).prune
        res = secret << 11  // multiply by 2048
        secret.mix(res).prune
    }

    private def generateNthNumber(seed: Long, n: Int): Long = {
        var currentSecret = seed
        for i <- 1 to n do
            currentSecret = generateNextNumber(currentSecret)
        currentSecret
    }

    var secretNumbers: Vector[Long] = Vector.empty

    def generateDifferences(seed: Long, n: Int): Vector[(Int, Int)] = {
        var currentSecret = seed
        val firstDigits = ArrayBuffer[Int]((seed % 10).toInt)
        val differences = ArrayBuffer[Int](0)
        val result = ArrayBuffer[(Int, Int)](((seed % 10).toInt, 0))
        for i <- 1 to n do {
            val newSecret = generateNextNumber(currentSecret)
            val newDigit = (newSecret % 10).toInt
            result += ((newDigit, newDigit - result.last._1))
            currentSecret = newSecret
        }
        result.toVector
    }

    override def part1: Any =
        secretNumbers = inputData.map(generateNthNumber(_, 2000))
        secretNumbers.sum


    override def part2: Any =
        0
}

object RandomNumberSimulator {

    // To mix a value into the secret number, calculate the bitwise XOR of the given value and the secret number.
    // Then, the secret number becomes the result of that operation.
    // (If the secret number is 42 and you were to mix 15 into the secret number, the secret number would become 37.)
    // To prune the secret number, calculate the value of the secret number modulo 16777216.
    // Then, the secret number becomes the result of that operation.
    // (If the secret number is 100000000 and you were to prune the secret number,
    // the secret number would become 16113920.)
    extension (i: Long) {
        def mix(j: Long): Long = i ^ j

        def prune: Long = i & 0xffffff  // modulo 16777216 (0x1000000
    }
}
