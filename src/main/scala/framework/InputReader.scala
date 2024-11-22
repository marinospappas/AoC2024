package org.mpdev.scala.aoc2024
package framework

import java.io.FileNotFoundException
import scala.io.Source.fromFile

object InputReader {

    private val filePattern = AocMain.environment match
        case "prod" => "src/main/resources/input/input"
        case _ => "src/test/resources/input/input"

    def read(day: Int, pattern: String = filePattern, extension: String = "txt"): List[String] = {
        if (AocMain.environment == "none")
            return List()
        val name =  f"$pattern$day%02d.$extension"
        val source = fromFile(name)
        try
            source.getLines().toList
        catch case ex: FileNotFoundException =>
            throw AoCException("Could not process file [" + name + "]" + ex.getMessage)
        finally
            source.close()
    }

}
