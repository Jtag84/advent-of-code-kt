package year2015.day18

import commons.Coordinates2d
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 17) { lightMap ->
    val numberOfSteps = if(isTest) {5} else {100}
    val maxX = lightMap.keys.maxOf { it.x }
    val maxY = lightMap.keys.maxOf { it.y }
    val lightMapWithStuckLights = lightMap.toMutableMap()
    lightMapWithStuckLights[Coordinates2d(0,0)] = Light.ON
    lightMapWithStuckLights[Coordinates2d(0,maxY)] = Light.ON
    lightMapWithStuckLights[Coordinates2d(maxX,0)] = Light.ON
    lightMapWithStuckLights[Coordinates2d(maxX,maxY)] = Light.ON
    generateSequence(lightMapWithStuckLights.toMap()) { nextLightsStatePart2(it, maxX, maxY) }.drop(numberOfSteps).take(1).last().count { it.value == Light.ON }
}

fun nextLightsStatePart2(lightMap: LightMap, maxX: Long, maxY : Long ) : LightMap {
    return lightMap.mapValues {
        val numberOfOnNeighbors = it.key.allAround().mapNotNull { neighBor -> lightMap[neighBor] }.count { it == Light.ON }
        when {
            it.key.x == 0L && it.key.y == 0L
                    || it.key.x == 0L && it.key.y == maxY
                    || it.key.x == maxX && it.key.y == 0L
                    || it.key.x == maxX && it.key.y == maxY -> Light.ON
            it.value == Light.ON && numberOfOnNeighbors in 2..3 -> Light.ON
            it.value == Light.OFF && numberOfOnNeighbors == 3 -> Light.ON
            else -> Light.OFF
        }
    }
}