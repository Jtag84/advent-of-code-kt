package year2023.day09

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 2L) { report ->
    report.map(::downToZerosList)
        .sumOf {
            it.map { l -> l.first() }
                .reduceRight(Long::minus)
        }
}

