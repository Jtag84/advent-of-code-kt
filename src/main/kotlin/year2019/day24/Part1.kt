package year2019.day24

import commons.Coordinates2d
import commons.Part.Companion.part1
import commons.cardinals

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

const val MAX_X = 5
const val MAX_Y = 5

val part1 = part1(inputParser, 2_129_920) { erisMap ->
    val bugs = erisMap.entries.filter { it.value == ErisMapElement.BUG }.map { it.key }.toSet()

    val alreadyGenerated : MutableSet<Set<Coordinates2d>> = mutableSetOf()
    generateSequence(bugs, Set<Coordinates2d>::passingMinute).first{
            val alreadySeen = alreadyGenerated.contains(it)
            alreadyGenerated.add(it)
            alreadySeen
        }
        .map { it.y * MAX_X + it.x }
        .sumOf { 1.shl(it.toInt()) }
}

fun Set<Coordinates2d>.passingMinute() : Set<Coordinates2d> {
    val aroundBugs = this.flatMap { it.cardinals()}.filter { it.x in 0..<MAX_X && it.y in 0..<MAX_Y }.toSet() - this

    val fromBugs = this.filter { it.cardinals().count { this.contains(it) } == 1 }
    val fromAroundBugs = aroundBugs.filter { it.cardinals().count { this.contains(it) } in (1..2) }

    return (fromBugs + fromAroundBugs).toSet()
}