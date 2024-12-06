package org.mpdev.scala.aoc2024
package solutions.day06

import utils.GridUtils

enum MapObject(val value: Char) {
    case GUARD extends MapObject('S')
    case OBSTACLE extends MapObject('#')
    case EMPTY extends MapObject('.')
    case ROUTE extends MapObject('X')
    case OBSTRUCTION extends MapObject('O')
    case ROUTE_UP extends  MapObject('^')
    case ROUTE_RIGHT extends  MapObject('>')
    case ROUTE_DOWN extends  MapObject('v')
    case ROUTE_LEFT extends  MapObject('<')
}

object MapObject {
    val mapper: Map[Char, MapObject] = MapObject.values.map(v => (v.value, v) ).toMap

    def ofDirection(dir: GridUtils.Direction): MapObject =
        MapObject.values.filter (o => o.value == dir.symbol).head
}
