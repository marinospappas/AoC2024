package org.mpdev.scala.aoc2024
package utils

import framework.AoCException

import java.util.Comparator
import scala.annotation.targetName
import scala.math.abs

case class Point(x: Int, y: Int) extends Comparable[Point] {

    def this(p: Point) = this(p.x, p.y)

    override def compareTo(other: Point): Int =
        // order is from top to bottom and from left to right
        // with y increasing downwards and x increasing to the right
        if (this.y == other.y)
            this.x.compareTo(other.x)
        else
            this.y.compareTo(other.y)

    @targetName("plus")
    def +(other: Point): Point =
        Point(this.x + other.x, this.y + other.y)

    @targetName("times")
    def *(n: Int): Point =
        Point(n * this.x, n * this.y)

    @targetName("minus")
    def -(other: Point): Point =
        Point(this.x - other.x, this.y - other.y)

    @targetName("div")
    def /(n: Int): Point =
        Point(this.x / n, this.y / n)

    def adjacent(diagonally: Boolean = true): Array[Point] =
        (if (diagonally)
            List(
                Point(x-1,y), Point(x-1,y-1), Point(x,y-1), Point(x+1,y-1),
                Point(x+1,y), Point(x+1,y+1), Point(x,y+1), Point(x-1,y+1)
            )
        else
            List(Point(x-1,y), Point(x,y-1), Point(x+1,y), Point(x,y+1)))
            .toArray()

    def adjacentCardinal(): Array[Point] = adjacent(false)

    def manhattan(other: Point): Int =
        abs(this.x - other.x) + abs(this.y - other.y)
    
    override def toString = s"Point($x,$y)"

    class ComparatorXY extends Comparator[Point] {
        override def compare(p1: Point, p2: Point): Int =
            if (p1 == null || p2 == null)
                throw AoCException ("unknown error - null point")
            if (p1.x < p2.x)
                return -1
            if (p1.x > p2.x)
                return 1
            Integer.compare(p1.y, p2.y)
    }
}
