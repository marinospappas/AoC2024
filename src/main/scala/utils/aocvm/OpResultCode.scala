package org.mpdev.scala.aoc2024
package utils.aocvm

enum OpResultCode {
    case SET_MEMORY
    case INCR_PC
    case SET_PC
    case INPUT
    case OUTPUT
    case EXIT
    case NONE
    case CUSTOM
}
