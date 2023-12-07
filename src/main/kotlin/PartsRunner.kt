import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.ParserResult
import cc.ekblad.konbini.parse
import com.google.common.base.Stopwatch
import kotlin.time.toKotlinDuration

class Part<T>(val partNumber: Int, val inputParser: Parser<T>, val solvingFunction: (T) -> Any) {
    companion object {
        fun <T> part1(inputParser: Parser<T>, solvingFunction: (T) -> Any): Part<T> {
            return Part(1, inputParser, solvingFunction)
        }

        fun part1(solvingFunction: (String) -> Any): Part<String> {
            return Part(1, parseAll, solvingFunction)
        }

        fun <T> part2(inputParser: Parser<T>, solvingFunction: (T) -> Any): Part<T> {
            return Part(2, inputParser, solvingFunction)
        }

        fun part2(solvingFunction: (String) -> Any): Part<String> {
            return Part(2, parseAll, solvingFunction)
        }

    }

    fun runTest(expected: Any) {
        val testInputs = solvingFunction.javaClass.readTestInputsPart(partNumber)
        val stopWatch = Stopwatch.createStarted()
        val result = solvingFunction(inputParser.parseOrThrowException(testInputs))
        stopWatch.stop()

        check(result == expected) {
            """
            Part $partNumber test: Failed in ${stopWatch.elapsed().toKotlinDuration()}
            expected $expected but was $result
        """.trimIndent()}

        "Part $partNumber test: Succeeded in ${stopWatch.elapsed().toKotlinDuration()}".println()
    }

    fun run() {
        val inputs = solvingFunction.javaClass.readInputs()
        val stopWatch = Stopwatch.createStarted()
        val parsedInputs = inputParser.parseOrThrowException(inputs)
        val parsingTime = stopWatch.elapsed()
        val result = solvingFunction(parsedInputs)
        stopWatch.stop()
        """
        Part $partNumber: $result 
        Parsed in ${parsingTime.toKotlinDuration()} Completed in ${stopWatch.elapsed().toKotlinDuration()}
    """.trimIndent().println()
    }

}

fun <T> Parser<T>.parseOrThrowException(input: String): T {
    val parseResult = this.parse(input)
    when (parseResult) {
        is ParserResult.Ok -> return parseResult.result
        else -> throw IllegalStateException(parseResult.toString())
    }
}

/**
 * Reads lines from the given input txt file.
 * object{}.javaClass.readInputs()
 */
fun Class<*>.readInputs(): String {
    return this.getResourceAsStream("inputs.txt")?.bufferedReader()?.readText()
        ?: throw IllegalStateException("File not found")
}

fun Class<*>.readTestInputsPart(partNumber: Int): String {
    return this.getResourceAsStream("test_inputs_part$partNumber.txt")?.bufferedReader()?.readText()
        ?: throw IllegalStateException("File not found")
}