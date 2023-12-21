import Headers.*
import commons.*

enum class Headers(val label: String) {
    DAY(" Day "),
    PART(" Part "),
    TEST_RESULT(" Test Result "),
    TEST_RUNTIME(" Test Runtime "),
    TEST_MEMORY(" Test Memory "),
    PARSING_TIME(" Parsing Time "),
    RUNTIME(" Runtime Duration "),
    MEMORY(" Memory Used "),
}

fun main() {
    printUsage()

    allParts.asSequence().map { part ->
        val (year, day) = Regex("year([0-9]{4}).day([0-3][0-9])").matchEntire(part.solvingFunction.javaClass.`package`.name)?.destructured!!
        Triple(year.toInt(), day.toInt(), part)
    }
        .groupBy { it.first }
        .forEach { (year, parts) -> printYear(year, parts) }
}

typealias Year = Int
typealias Day = Int

fun printYear(year: Int, parts: List<Triple<Year, Day, Part<out Any>>>) {
    """
        
    Year $year:
    
    ${Headers.entries.joinToString("|") { it.label }}
    """.trimIndent().println()

    parts
        .sortedWith(
            compareBy<Triple<Int, Int, Part<out Any>>> { it.first }
                .thenComparing(compareBy { it.second })
                .thenComparing(compareBy { it.third.partNumber })
        )
        .asSequence()
        .map { (_, day, part) -> toRow(part, day) }
        .forEach(::printRow)
}

private fun printRow(row: Map<Headers, String>) {
    entries.joinToString(" |") {
        val columnString = row[it] ?: ""
        val columnLength = it.label.length
        val ansiColorSize = if (columnString.contains('')) 9 else 0
        columnString.padStart(columnLength + ansiColorSize - 1)
    }.println()
}

private fun toRow(part: Part<out Any>, day: Day): Map<Headers, String> {
    val testData = part.runTest()?.let { (testResult, testParsingDuration, testRunningDuration, testMemory) ->
        listOf(
            TEST_RESULT to ((testResult == part.expectedTestResult).takeIf { it }?.let { "Passed".toGreenString() }
                ?: "Failed".toRedString()),
            TEST_RUNTIME to testRunningDuration.toColorizedString(),
            TEST_MEMORY to testMemory.toColorizedReadableMemorySize(),
        )
    }
    val (result, parsingDuration, runningDuration, memory) = part.run()

    return (listOf(
        DAY to "%02d".format(day),
        PART to "${part.partNumber}",
        PARSING_TIME to parsingDuration.toString(),
        RUNTIME to runningDuration.toColorizedString(),
        MEMORY to memory.toColorizedReadableMemorySize()
    ) + (testData ?: emptyList())).toMap()
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