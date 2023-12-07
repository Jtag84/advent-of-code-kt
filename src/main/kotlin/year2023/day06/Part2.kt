package year2023.day06

import Part.Companion.part2

fun main() {
    part2.runTest(71503)
    part2.run()
}

val part2 = part2(raceParser) { racePairs ->
    val (raceTime, record) = racePairs
        .reduce { (concatenatedRaceTime, concatenatedRecord), (raceTime, record) ->
            Pair(
                (concatenatedRaceTime.toString() + raceTime.toString()).toLong(),
                (concatenatedRecord.toString() + record.toString()).toLong()
            )
        }

    calculateButtonHoldingDistanceOutcomes(raceTime).filter { it > record }.count()
}