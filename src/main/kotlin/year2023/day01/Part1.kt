package year2023.day01

import commons.Part.Companion.part1

fun main() {
    part1.runTest()
    part1.run()
}

val part1 = part1(142) { lines ->
    lines
        .filter(String::isNotBlank)
        .sumOf {
            val leftDigit = getFirstDigitFromTheLeft(it)
            val rightDigit = getFirstDigitFromTheRight(it)
            leftDigit * 10 + rightDigit
        }
}

fun getFirstDigitFromTheLeft(line: String): Int {
    return line.first { it.isDigit() }.digitToInt()
}

fun getFirstDigitFromTheRight(line: String): Int {
    return line.last { it.isDigit() }.digitToInt()
}