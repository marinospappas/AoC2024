package org.mpdev.scala.aoc2024
package grid

import utils.GridBuilder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestGrid extends AnyFlatSpec {

    private val inputString = Vector(
        "012",
        "345",
        "678"
    )
    private val mapperChar = ('0' to '9').map( c => (c, c - '0')).toMap

    it should "read input from List[String] and setup int grid" in {
        val g = GridBuilder[Int]()
            .withMapper(mapperChar)
            .fromVisualGrid(inputString)
            .withPrintFormat("%c, ")
            .build()
        g.printIt()
    }
}
