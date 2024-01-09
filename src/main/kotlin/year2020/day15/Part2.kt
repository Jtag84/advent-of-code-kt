package year2020.day15

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 175594L) { startingNumbers ->
    getNthSpokenNumber(startingNumbers, 30_000_000)
}