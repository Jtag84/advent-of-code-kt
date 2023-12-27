import Headers.*
import commons.*
import kotlin.time.Duration

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

    val totalRow = parts
        .sortedWith(
            compareBy<Triple<Int, Int, Part<out Any>>> { it.first }
                .thenComparing(compareBy { it.second })
                .thenComparing(compareBy { it.third.partNumber })
        )
        .asSequence()
        .map { (_, day, part) -> toDataRow(part, day) }
        .map{
            rowToPrint(toStringRow(it)).println()
            it
        }
        .reduce { acc, row ->
            acc.merge(row) { a, b -> when{
                a is Duration && b is Duration -> a + b
                a is Long && b is Long -> a + b
                a is Int && b is Int -> a + b
                a is Boolean && b is Boolean -> a && b
                else -> throw IllegalStateException()
            }}
        }

    rowToPrint(toStringRow(totalRow)).replaceRange(0, 11, "      Total" ).println()
}

private fun rowToPrint(row: Map<Headers, String>) : String {
    return entries.joinToString(" |") {
        val columnString = row[it] ?: ""
        val columnLength = it.label.length
        val ansiColorSize = if (columnString.contains('')) 9 else 0
        columnString.padStart(columnLength + ansiColorSize - 1)
    }
}

private fun toDataRow(part: Part<out Any>, day: Day): Map<Headers, Any> {
    val testData = part.runTest()?.let { (testResult, testParsingDuration, testRunningDuration, testMemory) ->
        listOf(
            TEST_RESULT to (testResult == part.expectedTestResult),
            TEST_RUNTIME to testRunningDuration,
            TEST_MEMORY to testMemory,
        )
    }
    val (result, parsingDuration, runningDuration, memory) = part.run()

    return (listOf(
        DAY to day,
        PART to part.partNumber,
        PARSING_TIME to parsingDuration,
        RUNTIME to runningDuration,
        MEMORY to memory
    ) + (testData ?: emptyList())).toMap()
}

private fun toStringRow(dataRow: Map<Headers, Any>): Map<Headers, String> {
    return dataRow.mapValues {
        val value = it.value
        when {
            it.key == DAY -> "%02d".format(value)
            it.key == PART -> value.toString()
            it.key == TEST_RESULT && value is Boolean -> if(value) {"Passed".toGreenString()} else {"Failed".toRedString()}
            it.key == TEST_RUNTIME && value is Duration -> value.toColorizedString()
            it.key == TEST_MEMORY && value is Long -> value.toColorizedReadableMemorySize()
            it.key == PARSING_TIME && value is Duration -> value.toString()
            it.key == RUNTIME && value is Duration -> value.toColorizedString()
            it.key == MEMORY && value is Long -> value.toColorizedReadableMemorySize()
            else -> throw IllegalStateException()
        }
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