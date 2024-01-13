package year2020.day21

import com.google.common.collect.Sets
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 5) { foods ->
    val allergenToIngredientMap = getAllergenToIngredientMap(foods)

    val allIngredientsWithAllergens = allergenToIngredientMap.values.toSet()
    foods.map { it.first - allIngredientsWithAllergens }.sumOf { it.size }
}

fun getAllergenToIngredientMap(foods: List<Food>): MutableMap<String, String> {
    val allergenToIngredientMap = mutableMapOf<String, String>()
    val allergensWithoutMatchingIngredient = foods.flatMap { it.second }.distinct().toMutableList()

    while (allergensWithoutMatchingIngredient.isNotEmpty()) {
        val allergen = allergensWithoutMatchingIngredient.removeFirst()
        val allFoodWithAllergen = foods.filter { it.second.contains(allergen) }

        val allFoodWithAllergenRemoved = allFoodWithAllergen.map { (ingredients, allergens) ->
            val knownAllergens = allergens.filter { allergenToIngredientMap.contains(it) }.toSet()
            val ingredientsWithoutAllergens = ingredients - allergenToIngredientMap.values.toSet()

            ingredientsWithoutAllergens to allergens - knownAllergens
        }.toSet()

        val ingredientsWithAllergen = allFoodWithAllergenRemoved.map { it.first }.reduce(Sets::intersection)
        if (ingredientsWithAllergen.size == 1) {
            allergenToIngredientMap[allergen] = ingredientsWithAllergen.first()
        } else {
            allergensWithoutMatchingIngredient.add(allergen)
        }
    }
    return allergenToIngredientMap
}