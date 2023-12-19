package year2023.day12

import arrow.core.tail
import commons.Part.Companion.part1
import year2023.day12.SpringCondition.*

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 21L) { rows ->
    rows.sumOf { (springConditions, groupSizes) ->
        memoizedCountPossibleSpringConditions(springConditions, groupSizes)
    }
}

private val memoizationCache = hashMapOf<Pair<SpringConditions, GroupSizes>, Long>()

fun memoizedCountPossibleSpringConditions(springConditions: SpringConditions, groupSizes: GroupSizes): Long {
    return memoizationCache.getOrPut(springConditions to groupSizes) {
        when {
            springConditions.isEmpty() && groupSizes.isEmpty() -> 1L

            springConditions.isEmpty() && groupSizes.isNotEmpty() -> 0L

            springConditions.size < groupSizes.sum() + groupSizes.size - 1 -> 0

            springConditions.isNotEmpty() && groupSizes.isEmpty() -> if (springConditions.none { it == DAMAGED }) {
                1
            } else {
                0
            }

            springConditions.first() == OPERATIONAL -> memoizedCountPossibleSpringConditions(
                springConditions.tail(),
                groupSizes
            )

            springConditions.first() == UNKNOWN -> memoizedCountPossibleSpringConditions(
                listOf(DAMAGED) + springConditions.tail(),
                groupSizes
            ) + memoizedCountPossibleSpringConditions(springConditions.tail(), groupSizes)

            springConditions.take(groupSizes.first())
                .none { it == OPERATIONAL } && springConditions.drop(groupSizes.first()).take(1)
                .all { it != DAMAGED } -> {
                memoizedCountPossibleSpringConditions(springConditions.drop(groupSizes.first() + 1), groupSizes.tail())
            }

            else -> 0L
        }
    }
}