package org.mpdev.scala.aoc2024
package framework

import java.io.FileNotFoundException
import scala.io.Source.fromFile

object InputReader {

    private val filePattern = AocMain.environment match
        case "prod" => "src/main/resources/input/input"
        case "test" => "src/test/resources/input/input"
        case "none" => ""

    def read(day: Int, pattern: String = filePattern, extension: String = "txt"): Vector[String] = {
        if pattern.isEmpty then return Vector()
        val name =  f"$pattern$day%02d.$extension"
        val source = fromFile(name)
        try
            source.getLines().toVector
        catch case ex: FileNotFoundException =>
            throw AoCException("Could not process file [" + name + "]" + ex.getMessage)
        finally
            source.close()
    }

}
