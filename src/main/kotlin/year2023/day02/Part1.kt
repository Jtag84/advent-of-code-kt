package year2023.day02

import Part.Companion.part1

fun main() {
    part1.runTest()
    part1.run()
}

val part1 = part1(gamesParser, 8L) { games ->
    games.filter {
            it.getMaxRed() <= 12 && it.getMaxGreen() <= 13 && it.getMaxBlue() <= 14
        }
        .sumOf { it.gameId }
    }
