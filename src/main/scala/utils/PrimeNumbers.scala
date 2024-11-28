package org.mpdev.scala.aoc2024
package utils

import scala.collection.immutable.Set
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.boundary
import scala.util.boundary.break
import scala.math.{pow, sqrt, ceil}

object PrimeNumbers {

    var primes: List[Int] = List()

    def eratosthenesSieve(upperLimit: Int): List[Int] = {
        val sieve = Array.fill(upperLimit + 1) { false }
        val primeNumbers = ArrayBuffer(2)
        for (i <- 3 to upperLimit by 2) do {
            if (!sieve(i)) {
                primeNumbers += i
                for (j <- i.toLong * i to upperLimit.toLong by i.toLong) do
                    sieve(j.toInt) = true
            }
        }
        primes = primeNumbers.toList
        primes
    }

    def primeFactors(n: Int): Map[Int, Int] = {   // key = prime factor, value = exponent
        val pFactors = mutable.Map[Int, Int]()
        var number = n
        boundary:
            for (p <- primes) do {
                if (p * p > number)
                    break()
                while (number % p == 0) {
                    pFactors(p) = pFactors.getOrElseUpdate(p, 0) + 1
                    number /= p
                }
            }
        if (number > 1)
            pFactors(number) = pFactors.getOrElseUpdate(number, 0) + 1
        pFactors.toMap
    }

    def divisors2(number: Int): Set[Int] = {
        val upperLimit = ceil(sqrt(number.toDouble)).toInt
        val result = mutable.Set[Int]()
        for (d <- 1 to upperLimit) do {
            if (number % d == 0) {
                result.add(d)
                if (number / d != d)
                    result.add(number / d)
            }
        }
        result.toSet
    }

    def divisors(number: Int): List[Int] = {
        val primeFact = primeFactors(number)
        val result = ArrayBuffer[Int]()
        primeFact.foreach( (pf, exp) =>
            val thisDivisors = ArrayBuffer[Int]()
            var div = pf
            for (i <- 1 to exp) do {
                thisDivisors += div
                div *= pf
            }
            val combineWithPrevious = ArrayBuffer[Int]()
            for (prev <- result) do
                for (thisDiv <- thisDivisors) do
                    combineWithPrevious += prev * thisDiv
            result.addAll(thisDivisors)
            result.addAll(combineWithPrevious)
        )
        (result += 1).toList
    }

    def sigma(n: Int): Int = {
        var s = 1
        val primeF = primeFactors(n)
        for ( (key, value) <- primeF ) {
            s *= (pow(key.toDouble, (value + 1).toDouble).toInt - 1) / (key - 1)
        }
        s
    }


    /*
        fun divisors(number: Int): Set<Int> {
        val upperLimit = ceil(sqrt(number.toDouble())).toInt()
        val result = mutableSetOf<Int>()
        for (d in 1 .. upperLimit) {
            if (number % d == 0) {
                result.add(d)
                if (number / d != d)
                    result.add(number / d)
            }
        }
        return result
    }

    def eratosthenesSieve0(n: Int): List[Int] = {
        val sieve = Array(n + 1) { false }
        val primeNumbers = mutableListOf<Int>()
        for (i in 2 .. n ) {
            if (!sieve[i]) {
                primeNumbers.add(i)
                for (j in (2 * i) .. n step(i)) {
                    sieve[j] = true
                }
            }
        }
        primes = primeNumbers
        return primes
    }
     */
}

extension (i: Int)
    def isPrime: Boolean = PrimeNumbers.primes.contains(i)

extension (l: Long)
    def isPrime: Boolean = PrimeNumbers.primes.contains(l.toInt)