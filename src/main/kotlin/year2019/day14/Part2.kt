package year2019.day14

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

const val ORE_AMOUNT = 1_000_000_000_000L

val part2 = part2(inputParser, 460664L) { reactions ->
    val minOrePerFuel = reactions.calculateMinimumOreFor(1)

    var potentialFuelAmount = ORE_AMOUNT / minOrePerFuel
    var potentialOreUsed = potentialFuelAmount * minOrePerFuel

    generateSequence(potentialFuelAmount to potentialOreUsed) {(fuelAmount, oreUsed) ->
        val newOreUsed = reactions.calculateMinimumOreFor(fuelAmount)
        ORE_AMOUNT * fuelAmount / newOreUsed to newOreUsed
    }
        .zipWithNext()
        .first { it.first.first == it.second.first }
        .first.first
}