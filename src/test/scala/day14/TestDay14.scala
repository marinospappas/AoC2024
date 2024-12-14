package org.mpdev.scala.aoc2024
package day14

import framework.{AocMain, InputReader}
import solutions.day14.{RobotSimulator, Robot}
import solutions.day14.RobotSimulator.{MAX_X, MAX_Y}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay14 extends AnyFlatSpec {

    AocMain.environment = "test" +
        ""
    private var solver = RobotSimulator()

    it should "read input and setup robots" in {
        solver.robots.foreach(println)
        println(s"max x: ${RobotSimulator.MAX_X}, max y : ${RobotSimulator.MAX_Y}")
        (solver.robots.size, RobotSimulator.MAX_X, RobotSimulator.MAX_Y) shouldBe (12, 10, 6)
    }

    it should "calculate robot's movement with time" in {
        val robot = Robot((1, 1), (1, -1))
        val expected = List(
            (2,0), (3,3), (0,2), (1,1), (2,0), (3,3), (0,2), (1,1),
            (2,0), (3,3), (0,2), (1,1), (2,0), (3,3), (0,2), (1,1)
        )
        RobotSimulator.MAX_X = 3
        RobotSimulator.MAX_Y = 3
        val positions = for i <- 1 to 16 yield robot.curPos(i)
        println(positions)
        positions shouldBe expected
    }

    it should "calculate robot's current position with time" in {
        solver = RobotSimulator()
        val robot = Robot((2, 4), (2, -3))
        val expected = List( (4, 1), (6, 5), (8, 2), (10, 6), (1, 3) )
        println(robot)
        val positions = for i <- 1 to 5 yield robot.curPos(i)
        println(positions)
        positions shouldBe expected   
    }
    
    it should "solve part1 correctly" in {
        solver = RobotSimulator()
        solver.part1 shouldBe 12
    }
        
}
