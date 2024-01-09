package year2015.day18

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 4) { lightMap ->
    val numberOfSteps = if(isTest) {4} else {100}
    generateSequence(lightMap) { nextLightsState(it) }.drop(numberOfSteps).take(1).last().count { it.value == Light.ON }
}

fun nextLightsState(lightMap: LightMap) : LightMap {
    return lightMap.mapValues {
        val numberOfOnNeighbors = it.key.allAround().mapNotNull { neighBor -> lightMap[neighBor] }.count { it == Light.ON }
        when {
            it.value == Light.ON && numberOfOnNeighbors in 2..3 -> Light.ON
            it.value == Light.OFF && numberOfOnNeighbors == 3 -> Light.ON
            else -> Light.OFF
        }
    }
}