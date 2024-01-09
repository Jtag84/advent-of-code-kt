package year2020.day16

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 71L) { (fields, _, nearbyTickets) ->
    val ranges = fields.values.flatten()
    nearbyTickets.flatten().filterNot { number -> ranges.any { it.contains(number) } }.sum()
}