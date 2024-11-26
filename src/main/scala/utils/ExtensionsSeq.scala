package org.mpdev.scala.aoc2024
package utils

extension[T] (seq: Seq[T])

    def combinationsWithDuplicates(n: Int): Iterator[List[T]] = {
        val mappedSeq = seq.indices.map(i => ( i, seq(i)) )
        mappedSeq.combinations(n).map( _.map( x => x._2 ).toList )
    }
