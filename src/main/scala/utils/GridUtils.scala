package org.mpdev.scala.aoc2024
package utils

object GridUtils {

    enum Direction(val increment: Point, val symbol: Char = 'x') {
        // cardinal
        case UP extends Direction(Point(0, -1), '^')
        case N extends Direction(Point(0, -1))
        case RIGHT extends Direction(Point(1, 0), '>')
        case E extends Direction(Point(1, 0))
        case DOWN extends Direction(Point(0, 1), 'v')
        case S extends Direction(Point(0, 1))
        case LEFT extends Direction(Point(-1, 0), '<')
        case W extends Direction(Point(-1, 0))
        // diagonal
        case UP_RIGHT extends Direction(Point(1, -1))
        case NE extends Direction(Point(1, -1))
        case UP_LEFT extends Direction(Point(-1, -1))
        case NW extends Direction(Point(-1, -1))
        case DOWN_RIGHT extends Direction(Point(1, 1))
        case SE extends Direction(Point(1, 1))
        case DOWN_LEFT extends Direction(Point(-1, 1))
        case SW extends Direction(Point(-1, 1))
        
        case NONE extends Direction(Point(0, 0))
        
        def turn (leftRight: Direction): Direction = if leftRight == RIGHT then turnRight else turnLeft

        def turn (leftRight: Int): Direction = if (leftRight == 1) turnRight else turnLeft

       // extension( dir: Direction ) {
            def turnRight: Direction = this match
                case UP => RIGHT
                case RIGHT => DOWN
                case DOWN => LEFT
                case LEFT => UP
                case _ => NONE

            def turnLeft: Direction = this match
                case UP => LEFT
                case LEFT => DOWN
                case DOWN => RIGHT
                case RIGHT => UP
                case _ => NONE

            def reverse: Direction = this match
                case UP => DOWN
                case LEFT => RIGHT
                case DOWN => UP
                case RIGHT => LEFT
                case _ => NONE    

        //}

        /*
         fun toString1() = when(this) {
            UP -> "U";
            RIGHT -> "R";
            DOWN -> "D";
            LEFT -> "L"
        }

        fun toString2() = when(this) {
            UP -> "UP";
            RIGHT -> "RIGHT";
            DOWN -> "DOWN";
            LEFT -> "LEFT"
        }

        fun toString3() = when(this) {
            UP -> "N";
            RIGHT -> "E";
            DOWN -> "S";
            LEFT -> "W"
        }

        fun toString4() = when(this) {
            UP -> "NORTH";
            RIGHT -> "EAST";
            DOWN -> "SOUTH";
            LEFT -> "WEST"
        }

        fun toString5() = when(this) {
            UP -> "^";
            RIGHT -> ">";
            DOWN -> "v";
            LEFT -> "<"
        }
        */
    }
    
    object Direction {
        def allCardinal: Set[Direction] = Set(N, E, S, W)
        def allDirections: Set[Direction] = allCardinal ++ Set(NE, SE, SW, NW)
        
        /*
        fun of (s: String): Direction
            =
            when(s) {
                "U"
                , "UP"
                , "N"
                , "NORTH"
                , "^" -> UP
                "R"
                , "RIGHT"
                , "E"
                , "EAST"
                , ">" -> RIGHT
                "D"
                , "DOWN"
                , "S"
                , "SOUTH"
                , "v" -> DOWN
                "L"
                , "LEFT"
                , "W"
                , "WEST"
                , "<" -> LEFT
                else ->
                throw AocException("invalid Direction: [$s]")
            }

            fun of (c: Char): Direction
            = of(c.toString())
            fun of (inc: Point): Direction
            =
            values().firstOrNull {
                it.increment == inc
            }
            ?:
            throw AocException("invalid Direction increment: [$inc]")
         */
    }
}
