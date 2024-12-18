package org.mpdev.scala.aoc2024
package solutions.day18

enum MemoryState(val value: Char) {
    case CORRUPTED extends MemoryState('#')
    case PATH extends MemoryState('o')
}

object MemoryState {
    val mapper: Map[Char, MemoryState] = MemoryState.values.map( v => (v.value, v) ).toMap
}
