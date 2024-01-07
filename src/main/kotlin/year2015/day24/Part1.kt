package year2015.day24

import com.google.common.collect.Sets
import commons.Part.Companion.part1
import commons.combinations

fun main() {
    part1.runAndPrint()
}

val part1 = part1(inputParser, null) { weights ->
    val totalWeight= weights.sum()
    val weightPerGroup = totalWeight / 3

    // looking at the inputs, we can quickly see that getting the 5 heaviest and 1 you get to 1/3 of the total weight, so the minimum number of weights is 6
    Sets.combinations(weights.toSet(), 6)
        .filter { it.sum() == weightPerGroup }
        .map{it.reduce(Math::multiplyExact)}
        .min()
}