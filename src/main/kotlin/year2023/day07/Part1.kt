package year2023.day07

import Part.Companion.part1

fun main() {
    part1.runTest(6440L)
    part1.run()
}

val part1 = part1(inputParser) { handBidPairs ->
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