package year2015.day17

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import commons.Part.Companion.part1
import commons.sublists

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 4) { containers ->
    val targetLiters = if(isTest) { 25 } else { 150 }
    countContainerCombinationsWithTargetCapacity(containers.sorted(), emptyList(), 0, targetLiters).count()
}

fun countContainerCombinationsWithTargetCapacity(containers : List<Int>, currentContainers: List<Int>, currentCapacity: Int, targetCapacity: Int) : List<List<Int>> {
    return when {
        currentCapacity == targetCapacity -> listOf(currentContainers)
        currentCapacity > targetCapacity -> emptyList()
        containers.isEmpty() -> emptyList()
        else -> containers.takeWhile { it + currentCapacity <= targetCapacity }
            .flatMapIndexed { index, container -> countContainerCombinationsWithTargetCapacity(containers.subList(index + 1, containers.size), currentContainers+container, currentCapacity + container, targetCapacity) }
    }
}