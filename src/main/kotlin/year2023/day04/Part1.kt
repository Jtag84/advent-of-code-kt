package year2023.day04

import commons.Part.Companion.part1
import kotlin.math.pow

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(scratchcardsParser, 13L) { myWinningNumbers ->
    myWinningNumbers
        .map(::calculatePoints)
        .sum()
}

fun calculatePoints(myWinningNumbers: Set<Long>): Long {
    if (myWinningNumbers.isEmpty()) {
        return 0
    }

    return 2.0.pow(myWinningNumbers.size - 1.0).toLong()
}