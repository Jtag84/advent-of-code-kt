import Headers.*
import commons.*
import org.apache.commons.io.FileUtils
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

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
    val (testResult, testParsingDuration, testRunningDuration, testMemory) = part.runTest()
    val (result, parsingDuration, runningDuration, memory) = part.run()

    return mapOf(
        DAY to "%02d".format(day),
        PART to "${part.partNumber}",
        TEST_RESULT to ((testResult == part.expectedTestResult).takeIf { it }?.let { "Passed".toGreenString() }
            ?: "Failed".toRedString()),
        TEST_RUNTIME to getColorizedDuration(testRunningDuration),
        TEST_MEMORY to getColorizedReadableMemory(testMemory),
        PARSING_TIME to parsingDuration.toString(),
        RUNTIME to getColorizedDuration(runningDuration),
        MEMORY to getColorizedReadableMemory(memory)
    )
}

private fun getColorizedReadableMemory(memory: Long): String {
    val readableMemory = FileUtils.byteCountToDisplaySize(memory)
    return when {
        memory > 200_000_000 -> readableMemory.toRedString()
        memory > 50_000_000 -> readableMemory.toYellowString()
        else -> readableMemory.toGreenString()
    }
}

private fun getColorizedDuration(duration: kotlin.time.Duration): String {
    return when {
        duration > 1.seconds -> duration.toRedString()
        duration > 500.milliseconds -> duration.toYellowString()
        else -> duration.toGreenString()
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