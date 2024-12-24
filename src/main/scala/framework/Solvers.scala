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
import solutions.day16.ReindeerMinRoute
import solutions.day17.ByteComputer17
import solutions.day18.MemoryMaze
import solutions.day19.StripePatterns
import solutions.day20.RaceCondition
import solutions.day21.RoboticArmController
import solutions.day22.RandomNumberSimulator
import solutions.day23.InterconnectedComputers
import solutions.day0.Day0Solver
import solutions.day24.LogicalCircuit

val solvers = Map(
    1 -> LocationAnalyser(), 2 -> ReportAnalyser(), 3 -> CorruptedMemoryCleaner(), 4 -> WordSearch(), 5 -> PrintingRules(),
    6 -> MapRouteExplorer(), 7 -> BridgeRepair(), 8 -> AntennaPositionAnalyser(), 9 -> DiskFragmenter(), 10 -> TrailFinder(),
    11 -> PebbleTransformation(), 12 -> FenceCalculator(), 13 -> ClawMachinePlayer(), 14 -> RobotSimulator(), 15 -> WarehouseRobot(),
    16 -> ReindeerMinRoute(), 17 -> ByteComputer17(), 18 -> MemoryMaze(), 19 -> StripePatterns(), 20 -> RaceCondition(),
    21 -> RoboticArmController(), 22 -> RandomNumberSimulator(), 23 -> InterconnectedComputers(), 24 -> LogicalCircuit(), 25 -> Day0Solver()
)

val results = Map(
    1 -> ("1660292", "22776016"), 2 -> ("236", "308"), 3 -> ("190604937", "82857512"), 4 -> ("2536", "1875"), 5 -> ("5948", "3062"),
    6 -> ("5331", "1812"), 7 -> ("1298103531759", "140575048428831"), 8 -> ("276", "991"), 9 -> ("6399153661894", "6421724645083"), 10 -> ("510", "1058"),
    11 -> ("203228", "240884656550923"), 12 -> ("1473408", "886364"), 13 -> ("36870", "78101482023732"), 14 -> ("225648864", "7847"), 15 -> ("1429911", "1453087"),
    16 -> ("85420", "492"), 17 -> ("1,2,3,1,3,2,5,3,1", "105706277661082"), 18 -> ("280", "28,56"), 19 -> ("293", "623924810770264"), 20 -> ("1402", "1020244"), 
    21 -> ("0", "0"), 22 -> ("0", "0"), 23 -> ("0", "0"), 24 -> ("0", "0"), 25 -> ("0", "End of AoC 2025 - Merry Christmas")
)
