package org.mpdev.scala.aoc2024
package utils.aocvm

import utils.aocvm.IoChannel.READ_TIMEOUT

import java.util.concurrent.{LinkedTransferQueue, TimeUnit}


class IoChannel[T] {
    
    private val queue = LinkedTransferQueue[T]()
    
    def send(x: T): Unit = queue.add(x)
    
    def receive: T = queue.poll(READ_TIMEOUT, TimeUnit.MILLISECONDS)
        
    def receiveNonBlocking: T = queue.poll()
    
    def isEmpty: Boolean = queue.isEmpty
}

object IoChannel {
    val READ_TIMEOUT = 10000
}