package year2023.day15

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 1320) { input ->
    input.sumOf(::hash)
}

fun hash(string: String): Int {
    return string.toCharArray()
        .map { it.code }
        .fold(0) { acc, currentChar -> ((acc + currentChar) * 17) % 256 }
}