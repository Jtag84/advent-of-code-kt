package year2023.day06

import Part.Companion.part1

fun main() {
    part1.runTest(288)
    part1.run()
}

val part1 = part1(raceParser) { racePairs ->
    racePairs
        .map { (raceTime, recordDistance) ->
            calculateButtonHoldingDistanceOutcomes(raceTime).count { it > recordDistance }
        }
        .reduce { leftValue, rightValue -> leftValue * rightValue }
}

fun calculateButtonHoldingDistanceOutcomes(raceTime: Long): Sequence<Long> {
    return (1..<raceTime).asSequence().map { buttonHoldingTime ->
        val raceTimeLeft = raceTime - buttonHoldingTime
        buttonHoldingTime * raceTimeLeft
    }
}