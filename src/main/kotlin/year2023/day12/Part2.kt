package year2023.day12

import commons.Part.Companion.part2
import commons.repeat
import year2023.day12.SpringCondition.UNKNOWN

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 525152L) { rows ->
    rows.sumOf { (springConditions, groupSizes) ->
        val unfoldedSpringConditions =
            (springConditions + listOf(UNKNOWN)).asSequence().repeat().take(springConditions.size * 5 + 4).toList()
        val unfoldedGroupSizes = groupSizes.asSequence().repeat().take(groupSizes.size * 5).toList()
        memoizedCountPossibleSpringConditions(unfoldedSpringConditions, unfoldedGroupSizes)
    }
}

