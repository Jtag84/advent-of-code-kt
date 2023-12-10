import commons.println

fun main() {
    printUsage()

    allParts.forEach { part ->
        val parsedYearAndDay =
            Regex("year([0-9]{4}).day([0-3][0-9])").matchEntire(part.solvingFunction.javaClass.`package`.name)?.destructured!!
        val year = parsedYearAndDay.component1().toInt()
        val day = parsedYearAndDay.component2().toInt()

        """
            Running $year $day ${part.partNumber}:
        """.trimIndent()
            .println()
    }
}

fun printUsage() {
    println(
        """
        |aoc-kt  
        |   java -jar aoc-kt
        |
        """.trimMargin()
    )
}