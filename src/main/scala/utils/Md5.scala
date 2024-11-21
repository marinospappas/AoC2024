package org.mpdev.scala.aoc2024
package utils

import java.security.MessageDigest

object Md5 {

    private val md5: MessageDigest = MessageDigest.getInstance("MD5")

    def checksum(s: String): Array[Byte] =
        md5.update(s.toCharArray.map(_.toByte))
        md5.digest()

}

extension (ba: Array[Byte])
    private def toHexString: String = {
        val hexChars = "0123456789abcdef".toCharArray
        ba.flatMap(b =>
            val byte = b.toInt
            s"${hexChars(byte >>> 4 & 0x0F)}${hexChars(byte & 0x0F)}"
        ).mkString("")
    }
