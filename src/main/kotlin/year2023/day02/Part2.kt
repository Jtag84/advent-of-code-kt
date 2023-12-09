package year2023.day02

import Part.Companion.part2

fun main() {
    part2.runTest()
    part2.run()
}

val part2 = part2(gamesParser, 2286L) { games ->
    games.sumOf {
        it.getMaxRed() * it.getMaxGreen() * it.getMaxBlue()
    }
}

