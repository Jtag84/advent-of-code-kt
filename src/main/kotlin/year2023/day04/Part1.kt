package year2023.day04

import Part1Function
import cc.ekblad.konbini.*
import com.google.common.collect.Sets
import parseOrThrowException
import run
import runTest
import kotlin.math.pow

fun main() {
    part1.runTest(13L)
    part1.run()
}

typealias WinningNumbers = Set<Long>
typealias MyNumbers = Set<Long>

val scratchcardParser: Parser<Pair<WinningNumbers, MyNumbers>> = parser {
    string("Card")
    whitespace()
    integer()
    char(':')
    whitespace()
    val leftNumbers = chain(integer, whitespace)
    val winningNumbers = leftNumbers.terms.toSet()
    whitespace()
    char('|')
    whitespace()
    val rightNumbers = chain(integer, whitespace)
    val myNumbers = rightNumbers.terms.toSet()
    Pair(winningNumbers, myNumbers)
}

val part1 = Part1Function { input ->
    input.lines()
        .asSequence()
        .filter(String::isNotBlank)
        .map { scratchcardParser.parseOrThrowException(it) }
        .map { Sets.intersection(it.first, it.second).toSet() }
        .map(::calculatePoints)
        .sum()
}

fun calculatePoints(myWinningNumbers: Set<Long>): Long {
    if (myWinningNumbers.isEmpty()) {
        return 0
    }

    return 2.0.pow(myWinningNumbers.size - 1.0).toLong()
}