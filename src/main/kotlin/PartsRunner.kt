import arrow.core.Tuple4
import cc.ekblad.konbini.Parser
import com.google.common.base.Stopwatch
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.apache.commons.io.FileUtils
import java.time.LocalDate
import java.time.Month.DECEMBER
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

typealias PartNumber = Int
typealias DatePart = Pair<LocalDate, PartNumber>

class Part<T>(
    private val partNumber: PartNumber,
    private val inputParser: Parser<T>,
    private val expectedTestResult: Any,
    val solvingFunction: (T) -> Any
) {

    companion object {
        private val instances = ConcurrentHashMap<DatePart, Part<*>>()

        fun getAllInstances() = instances

        fun <T : Any> part1(inputParser: Parser<T>, expectedTestResult: Any, solvingFunction: (T) -> Any): Part<T> {
            return part(1, inputParser, expectedTestResult, solvingFunction)
        }

        fun part1(expectedTestResult: Any, solvingFunction: (List<String>) -> Any): Part<List<String>> {
            return part(1, parseLines, expectedTestResult, solvingFunction)
        }

        fun <T : Any> part2(inputParser: Parser<T>, expectedTestResult: Any, solvingFunction: (T) -> Any): Part<T> {
            return part(2, inputParser, expectedTestResult, solvingFunction)
        }

        fun part2(expectedTestResult: Any, solvingFunction: (List<String>) -> Any): Part<List<String>> {
            return part(2, parseLines, expectedTestResult, solvingFunction)
        }

        private fun <T : Any> part(
            partNumber: PartNumber,
            inputParser: Parser<T>,
            expectedTestResult: Any,
            solvingFunction: (T) -> Any
        ): Part<T> {
            val parsedYearAndDay =
                Regex("year([0-9]{4}).day([0-3][0-9])").matchEntire(solvingFunction.javaClass.`package`.name)?.destructured!!
            val year = parsedYearAndDay.component1().toInt()
            val day = parsedYearAndDay.component2().toInt()
            val datePart = Pair(LocalDate.of(year, DECEMBER, day), partNumber)

            val part = Part(partNumber, inputParser, expectedTestResult, solvingFunction)

            instances[datePart] = part

            return part
        }
    }

    private suspend fun runPart(input: String): Tuple4<Any, Duration, Duration, Long> {
        val runtime = Runtime.getRuntime()

        val parsingTimeWatch = Stopwatch.createStarted()
        val parsedInput = inputParser.parseOrThrowException(input)
        parsingTimeWatch.stop()

        return runBlocking(Dispatchers.Default) {
            var absoluteMaxMemoryUsages = 0L
            val monitoringJob = launch {
                measureMemoryUsage(1L).collect {
                    if (it > absoluteMaxMemoryUsages) {
                        absoluteMaxMemoryUsages = it
                    }
                }
            }

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

    fun runTest() {
        val testInputs = solvingFunction.javaClass.readTestInputsPart(partNumber)
        val (result, parsingDuration, runningDuration, memory) = runBlocking { runPart(testInputs) }
        val memoryDisplay = FileUtils.byteCountToDisplaySize(memory)

        check(result == expectedTestResult) {
            """
            |
            |Part $partNumber test: Failed 
            |Parsing duration: $parsingDuration
            |Running duration: $runningDuration
            |Memory usage: $memoryDisplay
            |expected:
            |   $expectedTestResult 
            |but was:
            |${result.toString().prependIndent()}
            |
            """.trimMargin()
        }
        """
            |
            |Part $partNumber test: Succeeded 
            |Parsing duration: $parsingDuration
            |Running duration: $runningDuration
            |Memory usage: $memoryDisplay
        """.trimMargin().println()
    }

    fun run() {
        val input = solvingFunction.javaClass.readInputs()
        val (result, parsingDuration, runningDuration, memory) = runBlocking { runPart(input) }
        val memoryDisplay = FileUtils.byteCountToDisplaySize(memory)

        """
            |
            |Part $partNumber: $result
            |Parsing duration: $parsingDuration
            |Running duration: $runningDuration
            |Total duration:  ${parsingDuration + runningDuration}
            |Memory usage: $memoryDisplay
        """.trimMargin().println()
    }
}

suspend fun measureMemoryUsage(intervalMillis: Long): Flow<Long> = flow {
    val runtime = Runtime.getRuntime()
    while (true) {
        emit(runtime.totalMemory() - runtime.freeMemory())
        delay(intervalMillis)
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