package year2018.day14

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 2018) { scoresToMatch ->
    val scoresToMatchList = scoresToMatch.toString().map { it.digitToInt() }
    val finalScores = generateRecipeScores()
        .first{
            val lastDigits = it.first.takeLast(scoresToMatchList.size + 1)
            lastDigits.drop(1) == scoresToMatchList || lastDigits.dropLast(1) == scoresToMatchList
        }
        .first

    if(finalScores.takeLast(scoresToMatchList.size) == scoresToMatchList) {
        finalScores.size - scoresToMatchList.size
    }
    else { // in case of the last score being double digits and the matching sequence being right before the last digit
        finalScores.size - scoresToMatchList.size - 1
    }
}