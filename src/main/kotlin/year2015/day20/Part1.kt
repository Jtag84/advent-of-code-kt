package year2015.day20

import commons.Part.Companion.part1
import commons.combinations
import org.apache.commons.math3.primes.Primes

fun main() {
    part1.runAndPrint()
}

val part1 = part1(inputParser, null) { input ->
    generateSequence(input/500) {it +1}.map {
        val primeFactors = listOf(1) + Primes.primeFactors(it.toInt())
        val result = primeFactors.combinations().map { it.reduce(Math::multiplyExact) }.distinct()
        it to result.sum()*10
    }.first { it.second >= input }
}