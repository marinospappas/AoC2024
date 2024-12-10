package org.mpdev.scala.aoc2024
package day09

import framework.AocMain
import solutions.day09.DiskFragmenter
import utils.also
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class TestDay09 extends AnyFlatSpec {

    AocMain.environment = "test"
    private val solver = DiskFragmenter()

    it should "read input and setup list" in {
        println(solver.filesSize)
        println(solver.freeSpace)
        println(solver.diskBlocks)
        println(solver.disk)
        (solver.filesSize.size, solver.freeSpace.size,
            solver.diskBlocks.map(i => if i == -1 then "." else i.toString).mkString) shouldBe
            (10, 9, "00...111...2...333.44.5555.6666.777.888899")
         //          012345678901234567890123456789012345678901
         //          0         1         2         3         4
    }

    it should "defragment the disk - move individual blocks" in {
        val defragmented = solver.defragmentDisk
        println(defragmented)
        defragmented.map(i => if i == -1 then "." else i.toString).mkString shouldBe "0099811188827773336446555566.............."
    }

    it should "solve part1 correctly" in {
        val result = solver.part1.also(println)
        result shouldBe 1928
    }

    it should "map files and free spaces" in {
        val (files, freeMap, freeList) = solver.findFilesAndFreeBlocksIndexesAndSizes(solver.diskBlocks)
        println(files)
        println(s"freeMap: $freeMap")
        println(s"freeList: $freeList")
        println(solver.diskBlocks.map(i => if i == -1 then "." else i.toString).mkString)
        println(files.size)
        println(freeMap.size)
        println(freeList.size)
        solver.defragmentFileSystem.also(println)
    }

    it should "defragment the disk - move whole files" in {
        val defragmented = solver.defragmentDisk2
        println(defragmented)
        defragmented.map(i => if i == -1 then "." else i.toString).mkString shouldBe "00992111777.44.333....5555.6666.....8888.."
    }

    it should "solve part2 correctly" in {
        val result = solver.part2.also(println)
        result shouldBe 2858
    }
}