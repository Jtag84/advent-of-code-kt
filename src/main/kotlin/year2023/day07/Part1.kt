package year2023.day07

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 6440L) { handBidPairs ->
    handBidPairs
        .calculateTotalWinnings()
}

fun List<Pair<Hand, Bid>>.calculateTotalWinnings(): Long {
    return this
        .sortedBy { it.first }
        .reversed()
        .map { it.second }
        .withIndex()
        .sumOf { it.value * (it.index + 1) }
}