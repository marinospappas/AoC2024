package org.mpdev.scala.aoc2024
package utils

extension (c: Char)
    
    def isVowel: Boolean = Set('a', 'e', 'i', 'o', 'u').contains(c)

