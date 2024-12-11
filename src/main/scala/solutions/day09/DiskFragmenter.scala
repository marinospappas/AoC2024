package org.mpdev.scala.aoc2024
package solutions.day09

import framework.{InputReader, PuzzleSolver}
import solutions.day09.DiskFragmenter.*
import solutions.day09.DiskFragmenter.DiskBlock.*

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import scala.util.boundary.break

class DiskFragmenter extends PuzzleSolver {

    val inputData: Vector[Int] = InputReader.read(9).head.map( _.toInt - '0' ).toVector
    val filesSize: Vector[Int] = inputData.indices.filter(_ % 2 == 0).map( inputData(_) ).toVector
    val freeSpace: Vector[Int] = inputData.indices.filter(_ % 2 == 1).map( inputData(_) ).toVector
    val fileSystem: Vector[DiskBlock] = Vector(DiskBlock.File(0, filesSize.head, true)) ++
        freeSpace.indices.flatMap(i =>
            Vector(Free(freeSpace(i))) ++ Vector(File( i + 1, filesSize(i + 1), false))
        )

    def defragmentDisk: Vector[Int] = {
        val blocks = fileSystemToBlocks(fileSystem).to(ArrayBuffer)
        var (nextFile, nextFree) = (blocks.lastIndexWhere(_ >= 0), blocks.indexWhere(_ < 0))
        while nextFile > nextFree do {
            blocks(nextFree) = blocks(nextFile)
            blocks(nextFile) = FREE
            nextFree = blocks.indexWhere(_ < 0, nextFree)
            nextFile = blocks.lastIndexWhere(_ >= 0, nextFile)
        }
        blocks.toVector
    }

    private def findNextFreeBlock(disk: ArrayBuffer[DiskBlock], fileSize: Int, fileIndex: Int): (Int, Int) =
        var (indx, size) = (-1, -1)
        ( if { indx = disk.indexWhere ( {
            case File(_, _, _) => false
            case Free(freeSize) => size = freeSize
                freeSize >= fileSize
        })
            indx < fileIndex
        } then indx else -1, size)

    def defragmentFileSystem: Vector[DiskBlock] = {
        val newDisk = ArrayBuffer.from(fileSystem)
        var (fileIndexInDisk, fileId, fileSize) = (0, 0, 0)
        while { fileIndexInDisk = newDisk.lastIndexWhere {
                case File(id, size, false) =>
                    fileId = id; fileSize = size
                    true
                case File(_,_,true) => false
                case Free(_) => false
            }
            fileIndexInDisk >= 0
        }
        do {
            val (nextFreeIndexOnDisk, freeBlockSize) = findNextFreeBlock(newDisk, fileSize, fileIndexInDisk)
            if nextFreeIndexOnDisk > 0 then {   // move file and free up the space
                newDisk(fileIndexInDisk) = Free(fileSize)
                if freeBlockSize == fileSize then newDisk(nextFreeIndexOnDisk) = File(fileId, fileSize, true)
                else {
                    newDisk(nextFreeIndexOnDisk) = Free(freeBlockSize - fileSize)
                    newDisk.insert(nextFreeIndexOnDisk, File(fileId, fileSize, true))
                }
            }
            else    // file cannot be moved, mark it anyway
                newDisk(fileIndexInDisk) = File(fileId, fileSize, true)
        }
        newDisk.toVector
    }

    def fileSystemToBlocks(fileSystem: Vector[DiskBlock]): List[Int] = {
        val blocks = ArrayBuffer[Int]()
        fileSystem.foreach({
            case File(id, size, _) => blocks ++= ArrayBuffer.fill(size)(id)
            case Free(size) => blocks ++= ArrayBuffer.fill(size)(-1)
        })
        blocks.toList
    }

    override def part1: Any =
        defragmentDisk.filter(_ >= 0).zipWithIndex.foldLeft(0L)( (sum, curr) => sum + curr._1 * curr._2)

    override def part2: Any =
        fileSystemToBlocks(defragmentFileSystem)
            .zipWithIndex.foldLeft(0L)( (sum, cur) => sum + (if cur._1 > 0 then cur._1 * cur._2 else 0))
}

object DiskFragmenter {
    val FREE = -1

    enum DiskBlock {
        case File(id: Int, size: Int, defrag: Boolean)
        case Free(size: Int)
    }
}