import cc.ekblad.konbini.Parser
import com.google.common.base.Stopwatch
import kotlin.time.toKotlinDuration

class Part<T>(val partNumber: Int, val inputParser: Parser<T>, val solvingFunction: (T) -> Any) {
    companion object {
        fun <T> part1(inputParser: Parser<T>, solvingFunction: (T) -> Any): Part<T> {
            return Part(1, inputParser, solvingFunction)
        }

        fun part1(solvingFunction: (List<String>) -> Any): Part<List<String>> {
            return Part(1, parseLines, solvingFunction)
        }

        fun <T> part2(inputParser: Parser<T>, solvingFunction: (T) -> Any): Part<T> {
            return Part(2, inputParser, solvingFunction)
        }

        fun part2(solvingFunction: (List<String>) -> Any): Part<List<String>> {
            return Part(2, parseLines, solvingFunction)
        }

    }

    fun runTest(expected: Any) {
        val testInputs = solvingFunction.javaClass.readTestInputsPart(partNumber)
        val stopWatch = Stopwatch.createStarted()
        val result = solvingFunction(inputParser.parseOrThrowException(testInputs))
        stopWatch.stop()

        check(result == expected) {
            """
        |
        |Part $partNumber test: Failed in ${stopWatch.elapsed().toKotlinDuration()}
        |expected:
        |   $expected 
        |but was:
        |${result.toString().prependIndent()}
        |
        """.trimMargin()
        }

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
        |Part $partNumber: $result 
        |Parsed in ${parsingTime.toKotlinDuration()} Completed in ${stopWatch.elapsed().toKotlinDuration()}
        """.trimMargin().println()
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