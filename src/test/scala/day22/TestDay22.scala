package org.mpdev.scala.aoc2024
package day22

import framework.AocMain
import solutions.day22.RandomNumberSimulator
import solutions.day22.RandomNumberSimulator.{mix, prune}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay22 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = RandomNumberSimulator()

    it should "read input and setup reports list" in {
        solver.inputData.foreach(println(_))
        solver.inputData.size shouldBe 4
    }

    it should "mix a number into the secret number" in {
        42.mix(15) shouldBe 37
    }

    it should "prune the secret number" in {
        100000000.prune shouldBe 16113920
    }

    it should "simulate 10 rounds of pseudo-random number generation" in {
        val expected = List(
            15887950, 16495136, 527345, 704524, 1553684, 12683156, 11100544, 12249484, 7753432, 5908254
        )
        var current: Long = 123
        (for i <- 1 to 10 yield {
            current = solver.generateNextNumber(current)
            println(current)
            current
        }) shouldBe expected.map( _.toLong )
    }

    it should "solve part1 correctly" in {
        (solver.part1, solver.secretNumbers) shouldBe (37327623, Vector(8685429, 4700978, 15273692, 8667524))
    }

    it should "generate differences between secret numbers" in {
        val result = solver.generateDifferences(123, 9)
        result.foreach(println)
    }

    it should "map differences to quantities" in {
        val result = solver.generateDiffsToNumberMapping(solver.generateDifferences(123, 9))
        result.foreach(println)
    }

    it should "solve part2 correctly" in {
        val solver = RandomNumberSimulator(Vector("1","2","3","2024"))
        solver.part2 shouldBe ((-2,1,-1,3), 23)
    }
}
