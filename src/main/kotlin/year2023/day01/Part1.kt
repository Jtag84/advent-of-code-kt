package year2023.day01

import Part1Function
import run
import runTest

fun main() {
    part1.runTest(142)
    part1.run()
}

val part1 = Part1Function { inputs ->
    inputs.lines()
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
    return line.reversed().first { it.isDigit() }.digitToInt()
}