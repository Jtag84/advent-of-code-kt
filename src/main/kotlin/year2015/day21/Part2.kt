package year2015.day21

import com.google.common.collect.Sets
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrint()
}

val part2 = part2(inputParser, null) { bossStats ->
    Sets.cartesianProduct(weapons, armors, rings, rings).map { it.toSet() }.filterNot { isWinningAgainstBoss(bossStats, it) }.maxOf { it.sumOf { it.first } }
}