package org.mpdev.scala.aoc2024
package framework

import solutions.day01.LocationAnalyser
import solutions.day02.ReportAnalyser
import solutions.day03.CorruptedMemoryCleaner

val solvers = Map(
    1 -> LocationAnalyser(), 2 -> ReportAnalyser(), 3 -> CorruptedMemoryCleaner()
)
