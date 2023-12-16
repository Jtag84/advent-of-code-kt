package year2023.day13

import commons.Part.Companion.part1
import commons.transpose
import kotlin.math.min

fun main() {
    part1.runTest()
    part1.run()
}

val part1 = part1(inputParser, 405) { input ->
    val horizontalSymmetries = input.map(::findHorizontalSymmetry).sumOf { it.sumOf { (a, _) -> a } }
    val verticalSymmetries = input.map(::findVerticalSymmetry).sumOf { it.sumOf { (a, _) -> a } }

    verticalSymmetries + 100 * horizontalSymmetries
}

fun findHorizontalSymmetry(lines: List<String>): List<Pair<Int, Int>> {
    val possibleSymmetryStart = lines.withIndex().zipWithNext().filter { it.first.value == it.second.value }

    return possibleSymmetryStart.filter {
        val symmetrySize = min(it.first.index + 1, lines.size - it.second.index)
        lines.subList(it.first.index + 1 - symmetrySize, it.first.index) == lines.subList(
            it.second.index + 1,
            it.second.index + symmetrySize
        ).reversed()
    }
        .map { it.first.index + 1 to it.second.index + 1 }
}

fun findVerticalSymmetry(lines: List<String>): List<Pair<Int, Int>> {
    val transposedLines = lines.map { it.toCharArray().toList() }.transpose().map { it.joinToString("") }
    return findHorizontalSymmetry(transposedLines)
}
