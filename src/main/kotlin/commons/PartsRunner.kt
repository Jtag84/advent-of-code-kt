package commons

import arrow.core.Tuple4
import cc.ekblad.konbini.Parser
import com.google.common.base.Stopwatch
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toKotlinDuration

typealias PartNumber = Int

class Part<T>(
    val partNumber: PartNumber,
    private val inputParser: Parser<T>,
    val expectedTestResult: Any?,
    val solvingFunction: (T) -> Any
) {
    companion object {
        fun <T : Any> part1(inputParser: Parser<T>, expectedTestResult: Any?, solvingFunction: (T) -> Any): Part<T> {
            return Part(1, inputParser, expectedTestResult, solvingFunction)
        }

        fun part1(expectedTestResult: Any?, solvingFunction: (List<String>) -> Any): Part<List<String>> {
            return Part(1, parseLines, expectedTestResult, solvingFunction)
        }

        fun <T : Any> part2(inputParser: Parser<T>, expectedTestResult: Any?, solvingFunction: (T) -> Any): Part<T> {
            return Part(2, inputParser, expectedTestResult, solvingFunction)
        }

        fun part2(expectedTestResult: Any?, solvingFunction: (List<String>) -> Any): Part<List<String>> {
            return Part(2, parseLines, expectedTestResult, solvingFunction)
        }
    }

    private fun runPart(input: String): Tuple4<Any, Duration, Duration, Long> {
        val runtime = Runtime.getRuntime()

        val parsingTimeWatch = Stopwatch.createStarted()
        val parsedInput = inputParser.parseOrThrowException(input)
        parsingTimeWatch.stop()

        return runBlocking(Dispatchers.Default) {
            var absoluteMaxMemoryUsages = 0L
            val monitoringJob = launch {
                measureMemoryUsage(1.milliseconds).collect {
                    if (it > absoluteMaxMemoryUsages) {
                        absoluteMaxMemoryUsages = it
                    }
                }
            }

            delay(10.milliseconds)

            val initialMemory = runtime.totalMemory() - runtime.freeMemory()

            val functionWatch = Stopwatch.createStarted()
            val result = solvingFunction(parsedInput)
            functionWatch.stop()

            monitoringJob.cancelAndJoin()
            val maxMemoryUsage = absoluteMaxMemoryUsages - initialMemory

            Tuple4(
                result,
                parsingTimeWatch.elapsed().toKotlinDuration(),
                functionWatch.elapsed().toKotlinDuration(),
                maxMemoryUsage
            )
        }
    }

    fun runTest(): Tuple4<Any, Duration, Duration, Long>? {
        if (expectedTestResult == null) {
            return null
        }
        val testInputs = solvingFunction.javaClass.readTestInputsPart(partNumber)
        return runPart(testInputs)
    }

    fun runAndPrintTest() {
        runTest()?.let { (result, parsingDuration, runningDuration, memory) ->
            check(result == expectedTestResult) {
                """
                |
                |Part $partNumber test: ${"Failed".toRedString()} 
                |Parsing duration: $parsingDuration
                |Running duration: $runningDuration
                |Total duration:  ${parsingDuration + runningDuration}
                |Memory usage: ${memory.toColorizedReadableMemorySize()}
                |expected:
                |   $expectedTestResult 
                |but was:
                |${result.toString().prependIndent()}
                |
                """.trimMargin()
            }
            """
                |
                |Part $partNumber test: ${"Succeeded".toGreenString()} 
                |Parsing duration: ${parsingDuration.toColorizedString()}
                |Running duration: ${runningDuration.toColorizedString()}
                |Total duration:  ${(parsingDuration + runningDuration).toColorizedString()}
                |Memory usage: ${memory.toColorizedReadableMemorySize()}
            """.trimMargin().println()
        }
            ?: "No tests".toRedString().println()
    }

    fun run(): Tuple4<Any, Duration, Duration, Long> {
        val input = solvingFunction.javaClass.readInputs()
        return runPart(input)
    }

    fun runAndPrint() {
        val (result, parsingDuration, runningDuration, memory) = run()

        """
            |
            |Part $partNumber: ${result.toGreenString()}
            |Parsing duration: ${parsingDuration.toColorizedString()}
            |Running duration: ${runningDuration.toColorizedString()}
            |Total duration:  ${(parsingDuration + runningDuration).toColorizedString()}
            |Memory usage: ${memory.toColorizedReadableMemorySize()}
        """.trimMargin().println()
    }
}

suspend fun measureMemoryUsage(intervalDuration: Duration): Flow<Long> = flow {
    val runtime = Runtime.getRuntime()
    while (true) {
        emit(runtime.totalMemory() - runtime.freeMemory())
        delay(intervalDuration)
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