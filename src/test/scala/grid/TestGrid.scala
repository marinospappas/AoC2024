package org.mpdev.scala.aoc2024
package grid

import utils.GridBuilder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestGrid extends AnyFlatSpec {

    private val input = List(
        "012",
        "345",
        "678"
    )
    private val mapper = ('0' to '9').map( c => (c, c - '0')).toMap

    it should "read input and setup int grid" in {
        val g = GridBuilder[Int]().withMapper(mapper).fromVisualGrid(input).withPrintFormat("%c, ").build()
        g.printIt()
    }
}
