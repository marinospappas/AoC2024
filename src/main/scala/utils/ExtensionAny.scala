package org.mpdev.scala.aoc2024
package utils

extension (a: Any)

    def printLn(): Unit =
        println(a)

    def also( f: Any => Unit ): Any =
        f(a)
        a