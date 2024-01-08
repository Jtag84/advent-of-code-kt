package year2015.day25

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrint()
}

val part1 = part1(inputParser, null) { (row, column) ->
    val position = calculatePosition(row, column)
    generateSequence(20151125L) { (it * 252533) % 33554393  }.take(position.toInt()).last()
}

fun calculatePosition(row: Long, column: Long) : Long {
    return (1..<row).sum() + (row-1) * (column-1) + (1..column).sum()
}