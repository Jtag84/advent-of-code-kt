package year2023.day11

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 82000210L) { galaxyMap ->
    calculateDistanceSumBetweenGalaxies(galaxyMap, 1_000_000L - 1L)
}