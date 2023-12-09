package year2023.day06

import Part.Companion.part2

fun main() {
    part2.runTest()
    part2.run()
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