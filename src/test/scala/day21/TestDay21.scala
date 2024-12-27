package org.mpdev.scala.aoc2024
package day21

import framework.AocMain
import solutions.day21.RoboticArmController
import solutions.day21.RoboticArmController.ENTER

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.TableDrivenPropertyChecks.{forAll, forEvery}
import org.scalatest.prop.Tables.Table

class TestDay21 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = RoboticArmController()

    it should "read input and setup reports list" in {
        println(solver.numKeysGrid)
        println(solver.dirKeysGrid)
        println(solver.keypadCodes)
        solver.keypadCodes.size shouldBe 5
    }

    private val numToNumParams = Table(
        ("num1", "num2", "expected"),
        ('A', '0', "<A"),
        ('A', '3', "^A"),
        ('A', '6', "^^A"),
        ('A', '9', "^^^A"),
        ('A', '2', "<^A"),
        ('A', '1', "^<<A"),
        ('4', 'A', ">>vvA"),
        ('8', 'A', "vvv>A"),
        ('3', '7', "<<^^A"),
        ('4', '0', ">vvA"),
    )
    it should "get directions for numeric keypad" in {
        forAll(numToNumParams) { (n1: Char, n2: Char, expected: String) =>
            println(s"$n1 to $n2")
            RoboticArmController.fromKeyToKey(solver.numKeysGrid, n1, n2) shouldBe expected
        }
    }

    private val dirToDirParams = Table(
        ("num1", "num2", "expected"),
        ('A', '^', "<A"),
        ('A', '>', "vA"),
        ('A', 'v', "<vA"),
        ('A', '<', "v<<A"),
        ('^', 'A', ">A"),
        ('v', 'A', "^>A"),
        ('<', 'A', ">>^A"),
        ('<', '^', ">^A"),
        ('<', '>', ">>A"),
        ('<', '^', ">^A"),
    )
    it should "get directions for direction keypad" in {
        forAll(dirToDirParams) { (d1: Char, d2: Char, expected: String) =>
            println(s"$d1 to $d2")
            RoboticArmController.fromKeyToKey(solver.dirKeysGrid, d1, d2) shouldBe expected
        }
    }

    it should "map key code to 1st stage directions" in {
        RoboticArmController.directionsForKeypad(solver.numKeysGrid, ENTER + "029A") shouldBe "<A^A^^>AvvvA"
    }

    it should "map key code to 2nd stage directions" in {
        RoboticArmController.directionsForKeypad(solver.dirKeysGrid,
            ENTER + RoboticArmController.directionsForKeypad(solver.numKeysGrid, "A" + "029A")) shouldBe "v<<A>>^A<A>A<AAv>A^A<vAAA^>A"
    }

    private val codeToFinalParams = Table(
        ("code", "expected"),
        ("029A", "<vA<AA>>^AvAA<^A>Av<<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A"),
        ("980A", "<v<A>>^AAAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A"),
        ("179A", "<v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A"),
        ("456A", "<v<A>>^AA<vA<A>>^AAvAA<^A>A<vA>^A<A>A<vA>^A<A>A<v<A>A>^AAvA<^A>A"),
        ("379A", "<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A"),
    )
    it should "map key code to final directions" in {
        forEvery(codeToFinalParams) { (code: String, expected: String) =>
            println(s"input: $code")
            println(s"expected: $expected")
            val result = solver.transform(code)
            //    RoboticArmController.directionsForKeypad(solver.dirKeysGrid,
            //        ENTER + RoboticArmController.directionsForKeypad(solver.dirKeysGrid,
            //            ENTER + RoboticArmController.directionsForKeypad(solver.numKeysGrid, ENTER + code)))
            println(s"result:   $result")
            result shouldBe expected
        }
    }

    it should "solve part1 correctly" in {
        solver.part1 shouldBe 126384
    }

    it should "solve part2 correctly" in {
        solver.part2 shouldBe 0
    }
}
