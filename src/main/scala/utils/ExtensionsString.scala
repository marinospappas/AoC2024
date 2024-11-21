package org.mpdev.scala.aoc2024
package utils

extension (s: String)

    def containsSeqTwice(seqLen: Int, gapLen: Int): Boolean =
        val endIndex = if (gapLen >= 0) s.length - 1 - seqLen - gapLen else s.length - 1 - seqLen
        for (i <- 0 to endIndex)
            val seqToCheck = s.substring(i, i + seqLen)
            gapLen match
                case 0 => // seq repeated twice in a row
                    if (s.substring(i + seqLen, s.length).startsWith(seqToCheck))
                        return true
                case -1 => // seq repeated anywhere in the string (but not overlapping)
                    if (s.substring(i + seqLen, s.length).contains(seqToCheck))
                        return true
                case _ => // seq repeated after exactly gapLen chars
                    if (s.substring(i + seqLen + gapLen, s.length).startsWith(seqToCheck))
                        return true
        false

    def containsAny(strings: Set[String]): Boolean =
        for (str <- strings)
            if (s.contains(str))
                return true
        false
        

