package year2023.day01

import Part2Function
import run
import runTest

fun main() {
    part2.runTest(281)
    part2.run()
}

val part2= Part2Function { input ->
    input
        .filter { it.isNotBlank() }
        .sumOf {
            val leftDigit = getLeftSpelledOutOrPlainDigit(it)
            val rightDigit = getRightSpelledOutOrPlainDigit(it)
            val calibrationValue = leftDigit * 10 + rightDigit
            calibrationValue
        }

}

val digitRegex = Regex("[0-9]")

fun getLeftSpelledOutOrPlainDigit(line: String): Int {
    val indexOfFirstDigit = digitRegex.find(line)?.range?.first ?: line.length

    return line.substring(0, indexOfFirstDigit).substringFindAllDigits().firstOrNull()
        ?: line[indexOfFirstDigit].digitToInt()
}

fun getRightSpelledOutOrPlainDigit(line: String): Int {
    val indexOfLastDigit = digitRegex.findAll(line).lastOrNull()?.range?.last ?: 0

    return line.substring(indexOfLastDigit).substringFindAllDigits().lastOrNull()
        ?: line[indexOfLastDigit].digitToInt()
}

fun String.substringFindAllDigits(): Sequence<Int> {
    val regex = Regex("(?=(one|two|three|four|five|six|seven|eight|nine|zero))", RegexOption.IGNORE_CASE)
    return regex.findAll(this)
        .flatMap { it.groupValues }
        .filter { it.isNotBlank() }
        .map { it.spelledOutDigitToInt() }
}

fun String.spelledOutDigitToInt(): Int {
    return when (this.lowercase()) {
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        else -> throw IllegalStateException()
    }
}