package year2023.day02

import Part2Function
import parseOrThrowException
import run
import runTest

fun main() {
    part2.runTest(2286L)
    part2.run()
}

val part2 = Part2Function { inputs ->
    gamesParser.parseOrThrowException(inputs).sumOf {
        it.getMaxRed() * it.getMaxGreen() * it.getMaxBlue()
    }
}

