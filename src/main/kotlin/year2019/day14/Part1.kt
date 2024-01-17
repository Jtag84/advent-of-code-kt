package year2019.day14

import commons.Part.Companion.part1
import commons.lcm
import kotlin.math.abs

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 165L) { reactions ->
    reactions.calculateMinimumOreFor(1)
}

fun Map<Chemical, Pair<Long, Set<Pair<Long, Chemical>>>>.calculateMinimumOreFor(quantityNeeded: Long, chemical: Chemical = "FUEL", chemicalRemainders: MutableMap<Chemical, Long> = mutableMapOf()) : Long {
    if(chemical == "ORE") {
        return quantityNeeded
    }

    val (quantityProduced, fromChemicals) = this[chemical]!!

    val timesToProduce = Math.ceil(quantityNeeded / quantityProduced.toDouble()).toLong()
    val chemicalRemainder = timesToProduce * quantityProduced - quantityNeeded

    chemicalRemainders[chemical] = chemicalRemainders[chemical]?:0 + chemicalRemainder

    return fromChemicals.sumOf {
        val quantityMinusRemainder = it.first * timesToProduce - (chemicalRemainders.remove(it.second)?:0)
        if(quantityMinusRemainder <= 0) {
            chemicalRemainders[it.second] = abs(quantityMinusRemainder)
            return@sumOf 0
        } else {
            this.calculateMinimumOreFor(quantityMinusRemainder, it.second, chemicalRemainders) }
        }
}