package org.mpdev.scala.aoc2024
package framework

import solutions.day01.LocationAnalyser
import solutions.day02.ReportAnalyser
import solutions.day03.CorruptedMemoryCleaner
import solutions.day04.WordSearch
import solutions.day05.PrintingRules
import solutions.day06.MapRouteExplorer
import solutions.day07.BridgeRepair
import solutions.day08.AntennaPositionAnalyser
import solutions.day09.DiskFragmenter
import solutions.day10.TrailFinder
import solutions.day11.PebbleTransformation
import solutions.day12.FenceCalculator
import solutions.day13.ClawMachinePlayer
import solutions.day14.RobotSimulator
import solutions.day15.WarehouseRobot

val solvers = Map(
    1 -> LocationAnalyser(), 2 -> ReportAnalyser(), 3 -> CorruptedMemoryCleaner(), 4 -> WordSearch(), 5 -> PrintingRules(),
    6 -> MapRouteExplorer(), 7 -> BridgeRepair(), 8 -> AntennaPositionAnalyser(), 9 -> DiskFragmenter(), 10 -> TrailFinder(),
    11 -> PebbleTransformation(), 12 -> FenceCalculator(), 13 -> ClawMachinePlayer(), 14 -> RobotSimulator(), 15 -> WarehouseRobot()
)
