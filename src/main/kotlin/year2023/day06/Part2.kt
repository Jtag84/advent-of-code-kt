package year2023.day06

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(raceParser, 71503) { racePairs ->
    val (raceTime, record) = racePairs
        .reduce { (concatenatedRaceTime, concatenatedRecord), (raceTime, record) ->
            Pair(
                (concatenatedRaceTime.toString() + raceTime.toString()).toLong(),
                (concatenatedRecord.toString() + record.toString()).toLong()
            )
        }

    calculateButtonHoldingDistanceOutcomes(raceTime).filter { it > record }.count()
}