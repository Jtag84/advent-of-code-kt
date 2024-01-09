package year2020.day13

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 295L) { (departureTime, busIds) ->
    busIds.filterNotNull().map { it to (it - departureTime % it) }
        .minBy { it.second }
        .let { it.first * it.second }
}