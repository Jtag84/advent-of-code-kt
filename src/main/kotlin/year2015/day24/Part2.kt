package year2015.day24

import com.google.common.collect.Sets
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrint()
}

val part2 = part2(inputParser, null) { weights ->
    val totalWeight= weights.sum()
    val weightPerGroup = totalWeight / 4

    // similar approach than part 1, we can quickly see that we'd get to a fourth of the weight with 5 weights
    Sets.combinations(weights.toSet(), 5)
        .filter { it.sum() == weightPerGroup }
        .map{it.reduce(Math::multiplyExact)}
        .min()
}