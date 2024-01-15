package year2018.day14

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 5941429882L) { numberOfRecipes ->
    generateRecipeScores()
        .first{it.first.size >= numberOfRecipes + 10}
        .first
        .takeLast(10)
        .toList()
        .joinToString("")
        .toLong()
}

fun generateRecipeScores() = generateSequence(mutableListOf(3, 7) to (0 to 1)) { (currentScores, elvesIndices) ->
    val (index1, index2) = elvesIndices
    val recipe1 = currentScores[index1]
    val recipe2 = currentScores[index2]
    val score = recipe1 + recipe2
    if (score < 10) {
            currentScores.add(score)
        } else {
            currentScores.add(score / 10)
            currentScores.add(score % 10)
        }

    val newIndex1 = (index1 + 1 + recipe1) % currentScores.size
    val newIndex2 = (index2 + 1 + recipe2) % currentScores.size
    currentScores to (newIndex1 to newIndex2)
}