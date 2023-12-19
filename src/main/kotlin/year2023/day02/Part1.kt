package year2023.day02

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(gamesParser, 8L) { games ->
    games.filter {
            it.getMaxRed() <= 12 && it.getMaxGreen() <= 13 && it.getMaxBlue() <= 14
        }
        .sumOf { it.gameId }
    }
