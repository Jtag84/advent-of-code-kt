package year2015.day17

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 3) { containers ->
    val targetLiters = if(isTest) { 25 } else { 150 }
    val containerCombinations = countContainerCombinationsWithTargetCapacity(containers.sorted(), emptyList(), 0, targetLiters)
    val minCount = containerCombinations.minOf { it.count() }
    containerCombinations.count { it.size == minCount}
}