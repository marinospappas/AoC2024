package org.mpdev.scala.aoc2024
package framework

import solutions.day01.LocationAnalyser
import solutions.day02.ReportAnalyser
import solutions.day03.CorruptedMemoryCleaner
import solutions.day04.WordSearch
import solutions.day05.Day05Solver

val solvers = Map(
    1 -> LocationAnalyser(), 2 -> ReportAnalyser(), 3 -> CorruptedMemoryCleaner(), 4 -> WordSearch(), 5 -> Day05Solver()
)
