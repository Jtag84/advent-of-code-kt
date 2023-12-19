package year2023.day18

import commons.Direction.*
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 952408144115L) { digDirections ->
    calculateDigArea(digDirections.map(::correctDigDirection))
}

fun correctDigDirection(digDirection: DigDirection): DigDirection {
    val length = digDirection.color.dropLast(1).toLong(16)
    val direction = when (digDirection.color.last()) {
        '0' -> RIGHT
        '1' -> DOWN
        '2' -> LEFT
        '3' -> UP
        else -> throw IllegalStateException()
    }

    return DigDirection(direction, length, "")
}