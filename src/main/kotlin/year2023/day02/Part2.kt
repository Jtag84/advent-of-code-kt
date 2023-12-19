package year2023.day02

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(gamesParser, 2286L) { games ->
    games.sumOf {
        it.getMaxRed() * it.getMaxGreen() * it.getMaxBlue()
    }
}

