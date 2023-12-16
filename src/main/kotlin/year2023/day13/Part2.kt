package year2023.day13

import com.google.common.collect.Lists
import commons.Part.Companion.part2

fun main() {
    part2.runTest()
    part2.run()
}

val part2 = part2(inputParser, 400) { input ->
    input.map(::findNewSymmetry).toList()
        .sumOf { (horizontals, verticals) -> horizontals.sumOf { it.first } * 100 + verticals.sumOf { it.first } }
}

fun findNewSymmetry(lines: List<String>): Pair<List<Pair<Int, Int>>, List<Pair<Int, Int>>> {
    val beforeSmudgeCleanHorizontalSymmetry = findHorizontalSymmetry(lines)
    val beforeSmudgeCleanVerticalSymmetry = findVerticalSymmetry(lines)

    return getSmudgeCleanedSequence(lines).map { smudgeFreeLine ->
        findHorizontalSymmetry(smudgeFreeLine) to findVerticalSymmetry(smudgeFreeLine)
    }
        .map { (horizontals, verticals) ->
            horizontals.filterNot { beforeSmudgeCleanHorizontalSymmetry.contains(it) } to verticals.filterNot {
                beforeSmudgeCleanVerticalSymmetry.contains(
                    it
                )
            }
        }
        .filterNot { (horizontals, verticals) -> horizontals.isEmpty() && verticals.isEmpty() }
        .first()
}

private fun getSmudgeCleanedSequence(lines: List<String>): Sequence<List<String>> {
    val smudgeCoordinates = Lists.cartesianProduct(lines[0].indices.toList(), lines.indices.toList()).toList()

    return smudgeCoordinates.asSequence().map { (charPosition, lineNumber) ->
        lines.mapIndexed { indexLine, line ->
            if (indexLine == lineNumber) {
                line.mapIndexed { indexChar, currentChar ->
                    if (indexChar == charPosition) {
                        when (currentChar) {
                            '#' -> '.'
                            '.' -> '#'
                            else -> throw IllegalStateException()
                        }
                    } else {
                        currentChar
                    }
                }.joinToString("")
            } else {
                line
            }
        }
    }
}

