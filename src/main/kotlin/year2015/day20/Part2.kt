package year2015.day20

import commons.Part.Companion.part2
import commons.combinations
import org.apache.commons.math3.primes.Primes

fun main() {
    part2.runAndPrint()
}

val part2 = part2(inputParser, null) { input ->
    generateSequence(2) {it +1}.map { houseNumber ->
        val primeFactors = listOf(1) + Primes.primeFactors(houseNumber.toInt())
        val result = primeFactors.combinations().map { it.reduce(Math::multiplyExact) }.filter { it * 50 >= houseNumber }.distinct()
        houseNumber to result.sum()*11
    }.first { it.second >= input }
}