package year2019.day24

import commons.Coordinates
import commons.Coordinates2d
import commons.Direction
import commons.Part.Companion.part2
import commons.cardinals

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

typealias Level = Long
typealias RecursiveCoordinates = Pair<Coordinates2d, Level>

val part2 = part2(inputParser, 99) { erisMap ->
    val bugs = erisMap.entries.filter { it.value == ErisMapElement.BUG }.map { RecursiveCoordinates(it.key, 0) }.toSet()

    val numberOfMinutes = if(isTest) {10} else {200}
    generateSequence(bugs, Set<RecursiveCoordinates>::passingMinute).drop(numberOfMinutes).first().count()
}

val MIDDLE_COORDINATES = Coordinates2d(2L,2L)

fun RecursiveCoordinates.cardinals() : Set<RecursiveCoordinates> {
    return Direction.entries.flatMap { direction ->
        val newCoordinates = direction.move(this.first)
        when {
            newCoordinates == MIDDLE_COORDINATES && direction == Direction.UP -> {
                (0L..<MAX_X).map { RecursiveCoordinates(Coordinates2d(it, 4), this.second + 1) }
            }
            newCoordinates == MIDDLE_COORDINATES && direction == Direction.RIGHT -> {
                (0L..<MAX_Y).map { RecursiveCoordinates(Coordinates2d(0, it), this.second + 1) }
            }
            newCoordinates == MIDDLE_COORDINATES && direction == Direction.DOWN -> {
                (0L..<MAX_X).map { RecursiveCoordinates(Coordinates2d(it, 0), this.second + 1) }
            }
            newCoordinates == MIDDLE_COORDINATES && direction == Direction.LEFT -> {
                (0L..<MAX_Y).map { RecursiveCoordinates(Coordinates2d(4, it), this.second + 1) }
            }
            newCoordinates.x < 0 -> {
                listOf(RecursiveCoordinates(Coordinates2d(1, 2), this.second - 1))
            }
            newCoordinates.x >= MAX_X -> {
                listOf(RecursiveCoordinates(Coordinates2d(3, 2), this.second - 1))
            }
            newCoordinates.y < 0 -> {
                listOf(RecursiveCoordinates(Coordinates2d(2, 1), this.second - 1))
            }
            newCoordinates.y >= MAX_Y -> {
                listOf(RecursiveCoordinates(Coordinates2d(2, 3), this.second - 1))
            }
            else -> {
                listOf(RecursiveCoordinates(newCoordinates, this.second))
            }
        }
    }.toSet()
}

fun Set<RecursiveCoordinates>.passingMinute() : Set<RecursiveCoordinates> {
    val aroundBugs = this.flatMap { it.cardinals()}.toSet() - this

    val fromBugs = this.filter { it.cardinals().count { this.contains(it) } == 1 }
    val fromAroundBugs = aroundBugs.filter { it.cardinals().count { this.contains(it) } in (1..2) }

    return (fromBugs + fromAroundBugs).toSet()
}