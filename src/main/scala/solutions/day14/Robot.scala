package org.mpdev.scala.aoc2024
package solutions.day14

import solutions.day14.RobotSimulator
import solutions.day14.RobotSimulator.{MAX_X, MAX_Y}
import utils.*

import scala.math.abs

case class Robot(initPos: (Int, Int), velocity: (Int, Int)) {
    
    def curPos(time: Int): (Int, Int) =
        (moveInOneDim(initPos.x, velocity.x, time, RobotSimulator.MAX_X), moveInOneDim(initPos.y, velocity.y, time, RobotSimulator.MAX_Y))
    
    private def moveInOneDim(p: Int, v: Int, t: Int, max: Int): Int = {
        p + v.sign * (t * abs(v)) % (max + 1) match
            case newP if newP > max => newP - (max + 1)
            case newP if newP < 0 => max + 1 + newP
            case newP => newP
    }
}
