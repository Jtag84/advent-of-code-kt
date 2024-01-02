package year2015.day19

import arrow.core.tail
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 4) { (replacements, moleculeChain) ->
    (0..<moleculeChain.size).flatMap { replace(replacements, moleculeChain, it) }.distinct().count()
}

fun replace(replacements : Map<String, List<List<String>>>, moleculeChain : List<String>, indexToReplace : Int) : List<List<String>> {
    val toReplace = moleculeChain[indexToReplace]
    val replacementMolecules = replacements[toReplace]

    val mutableMoleculeChain = moleculeChain.toMutableList()
    return replacementMolecules?.map {
        mutableMoleculeChain.take(indexToReplace) + it + moleculeChain.drop(indexToReplace + 1)
    } ?: emptyList()
}