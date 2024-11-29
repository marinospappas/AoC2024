package org.mpdev.scala.aoc2024
package primenumbers

import utils.PrimeNumbers
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.TableDrivenPropertyChecks.*
import org.scalatest.prop.Tables.Table

class TestLcm extends AnyFlatSpec {

    {
        PrimeNumbers.eratosthenesSieve(1000000)
    }

    private val lcmParams = Table(
        ("input", "expected"),
        (Set(12, 24, 150), 600),
        (Set(18, 14, 9), 126),
        (Set(24, 16, 15), 240),
    )

    it should "calculate lcm correctly" in {
        forAll (lcmParams) { (input: Set[Int], expected: Int) =>
            PrimeNumbers.lcm(input).toInt shouldBe expected
        }
    }

    it should "perform multiple lcm calculations" in {
        var result = 0L
        val start = System.currentTimeMillis()
        for (i <- 1 to 1000000) do {
            result = PrimeNumbers.lcm(Set(12356, 84756, 998223, 123346))
        }
        println(s"$result - ${System.currentTimeMillis() - start}msecs")
    }

}
