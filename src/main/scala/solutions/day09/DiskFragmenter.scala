package org.mpdev.scala.aoc2024
package solutions.day09

import framework.{InputReader, PuzzleSolver}
import solutions.day09.DiskFragmenter.*

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import scala.util.boundary.break

class DiskFragmenter extends PuzzleSolver {

    val inputData: List[Int] = InputReader.read(9).head.map( _.toInt - '0' ).toList
    val filesSize: List[Int] = inputData.indices.filter(_ % 2 == 0).map( inputData(_) ).toList
    val freeSpace: List[Int] = inputData.indices.filter(_ % 2 == 1).map( inputData(_) ).toList
    val diskBlocks: List[Int] = List.fill(filesSize.head)(0) ++
        freeSpace.indices.flatMap( i =>
            List.fill(freeSpace(i))(-1) ++ List.fill(filesSize(i + 1))((i + 1).toChar)
        )

    var timeBuildingFreeAndFilesMap = 0L
    var timeLookingUpFreeBlocks = 0L
    var timeMovingFiles = 0L

    def defragmentDisk: List[Int] = {
        val blocks = diskBlocks.to(ArrayBuffer)
        val freeBlocksIndxs = diskBlocks.zipWithIndex.filter(_._1 == FREE).map(_._2)
        val fileBlocksIndxs = diskBlocks.zipWithIndex.filter(_._1 != FREE).map(_._2)
        var nextFreeIndex = 0
        var nextFileIndex = fileBlocksIndxs.size - 1
        var lastFileBlock = blocks.size
        while blocks.slice(freeBlocksIndxs(nextFreeIndex), lastFileBlock - 1).exists(_ != FREE) do {
            lastFileBlock = fileBlocksIndxs(nextFileIndex)
            blocks(freeBlocksIndxs(nextFreeIndex)) = blocks(lastFileBlock)
            blocks(lastFileBlock) = FREE
            nextFreeIndex += 1
            nextFileIndex -= 1
        }
        blocks.toList
    }

    def getFirstFreeBlock(freeMap: Map[Int, List[Int]], size: Int): (Int, Int, Int) = {
        boundary:
            for i <- size to 9 do {
                if freeMap.contains(i) && freeMap(i).exists(_ > 0) then
                    break((freeMap(i).head, i, 0))
            }
            (-1, -1, 0)
    }

    def adjustFreeMap(freeMap: Map[Int, List[Int]], key: Int, fileSize: Int): Map[Int, List[Int]] = {
        val newFreeMap = freeMap.to(mutable.Map)
        val freeBlocksList = newFreeMap(key)
        val updatedBlocksList = freeBlocksList.slice(1, freeBlocksList.size)
        val firstFreeBlock = newFreeMap(key).head
        if updatedBlocksList.isEmpty then newFreeMap.remove(key)
        else newFreeMap.put(key, freeBlocksList.slice(1, freeBlocksList.size))
        if key > fileSize then     // free space was > file size
            newFreeMap.put(key - fileSize,
                (freeMap.getOrElse(key - fileSize, List()) :+ (firstFreeBlock + fileSize)).sorted )
        newFreeMap.toMap
    }

    def getFirstFreeBlock(freeList: List[(Int, Int)], size: Int): (Int, Int, Int) = {
        boundary:
            for i <- freeList.indices do {
                if freeList(i)._2 >= size then
                    break((freeList(i)._1, freeList(i)._2, i))
            }
            (-1, -1, -1)
    }

    def adjustFreeList(freeList: List[(Int, Int)], index: Int, fileSize: Int): List[(Int, Int)] = {
        val newFreeList = freeList.to(ArrayBuffer)
        val (firstFreeBlock, firstFreeBlockSize) = newFreeList(index)
        if firstFreeBlockSize > fileSize then newFreeList(index) = (firstFreeBlock + fileSize, firstFreeBlockSize - fileSize)
        else newFreeList.remove(index)
        newFreeList.toList
    }

    def moveFile(blocks: ArrayBuffer[Int], from: Int, to: Int, length: Int): Unit = {
        val fileId = blocks(from)
        val start = System.nanoTime()
        for i <- 0 until length do {
            blocks(to + i) = fileId
            blocks(from + i) = FREE
        }
    }

    // TODO: fix bug caused by the introduction of the map of free blocks - affects part 2
    def defragmentDisk2: List[Int] = {
        val blocks = diskBlocks.to(ArrayBuffer)
        val (filesBlocks, freeBlocks, freeBlocksList) = findFilesAndFreeBlocksIndexesAndSizes(blocks.toList)
        var freeMap = freeBlocks
        var freeList = freeBlocksList
        var curFileIndx = filesBlocks.size - 1
        while {
            val (fileStart, fileSize) = filesBlocks(curFileIndx)
            // find first free block that fits the file
            val start = System.nanoTime()
            val (firstFreeBlock, firstFreeBlockSize, ignore) = getFirstFreeBlock(freeMap, fileSize)
            // val (firstFreeBlock, firstFreeBlockSize, freeListKey) = getFirstFreeBlock(freeList, fileSize)
            timeLookingUpFreeBlocks += (System.nanoTime() - start)
            if firstFreeBlockSize >= 0 then {
                // if such a block exists move the file
                moveFile(blocks, fileStart, firstFreeBlock, fileSize)
                timeMovingFiles += (System.nanoTime() - start)
                // and adjust the free map
                freeMap = adjustFreeMap(freeMap, firstFreeBlockSize, fileSize)
                // freeList = adjustFreeList(freeList, freeListKey, fileSize)
            }
            curFileIndx -= 1
            curFileIndx >= 0
        } do {}
        blocks.toList
    }

    def findFilesAndFreeBlocksIndexesAndSizes(diskBlocks: List[Int]): (List[(Int, Int)], Map[Int, List[Int]], List[(Int, Int)]) = {     // start index, size
        val start = System.nanoTime()
        val files = ArrayBuffer[(Int, Int)]((0, filesSize.head))       // position of block, size
        val freeList = ArrayBuffer[(Int, Int)]()
        val freeMap = mutable.Map[Int, List[Int]]()     // size of free block -> list of positions in the disk
        var curIndex = filesSize.head
        for i <- freeSpace.indices do {
            if freeSpace(i) > 0 then {
                freeMap.put(freeSpace(i), freeMap.getOrElse(freeSpace(i), List()) :+ curIndex)
                freeList += ((curIndex, freeSpace(i)))
            }
            curIndex += freeSpace(i)
            files += ((curIndex, filesSize(i + 1)))
            curIndex += filesSize(i + 1)
        }
        timeBuildingFreeAndFilesMap = System.nanoTime() - start
        (files.toList, freeMap.toMap, freeList.toList)
    }

    override def part1: Any =
        defragmentDisk.filter(_ != FREE).zipWithIndex.map(e => e._1.toLong * e._2).sum

    override def part2: Any =
        val result = defragmentDisk2
        val start = System.nanoTime()
        //val result1 = result.zipWithIndex.map(e => if e._1 == FREE then 0 else e._1.toLong * e._2.toLong).sum
        val result1 = result.zipWithIndex.filter(_._1 != FREE).foldLeft(0L)( (sum, cur) => sum + cur._1 * cur._2)
        val elapsed = System.nanoTime() - start
        println(s"Build free and files map: ${timeBuildingFreeAndFilesMap / 1000000}")
        println(s"Lookup free space       : ${timeLookingUpFreeBlocks / 1000000}")
        println(s"Move files              : ${timeMovingFiles / 1000000}")
        println(s"Calculate result        : ${elapsed / 1000000}")
        result1

    // input parsing
    private def readRule(s: String): (Int, Int) =
        s match { case s"${p1}|${p2}" => (p1.toInt, p2.toInt) }

    private def readPagesSection(s: String): List[Int] =
        s.split(",").map(a => a.toInt).toList
}

object DiskFragmenter {
    val FREE = -1
}