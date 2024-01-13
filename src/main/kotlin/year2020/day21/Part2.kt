package year2020.day21

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, "mxmxvkd,sqjhc,fvjkl") { foods ->
    val allergenToIngredientMap = getAllergenToIngredientMap(foods)
    allergenToIngredientMap.entries.sortedBy { it.key }.joinToString(",") { it.value }
}