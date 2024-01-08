package year2020.day10

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 35) { adapters ->
    val joltDifferences = (adapters + 0).sorted().zipWithNext { a, b -> b - a }.groupBy { it }.mapValues { it.value.size }
    joltDifferences[1]!! * (joltDifferences[3]!! + 1)
}