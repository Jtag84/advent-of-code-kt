package year2023.day02

import Part.Companion.part2

fun main() {
    part2.runTest(2286L)
    part2.run()
}

val part2 = part2(gamesParser) { games ->
    games.sumOf {
        it.getMaxRed() * it.getMaxGreen() * it.getMaxBlue()
    }
}

