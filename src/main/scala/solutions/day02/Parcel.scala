package org.mpdev.scala.aoc2024
package solutions.day02

case class Parcel(w: Int, l: Int, h: Int) {

    def area(): Int = 2*w*l + 2*l*h + 2*h*w

    def extra(): Int = w*l  min l*h min h*w

    def ribbon(): Int = (2*(w+l) min 2*(l+h) min 2*(h+w)) + w*l*h

}
