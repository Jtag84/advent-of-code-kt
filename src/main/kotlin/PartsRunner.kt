import com.google.common.base.Stopwatch
import kotlin.time.toKotlinDuration

fun interface PartFunction {
    operator fun invoke(inputs : List<String>): Any
}

fun interface Part1Function : PartFunction
fun interface Part2Function : PartFunction

fun Part1Function.run(){
    runPart(1)
}

fun Part2Function.run(){
    runPart(2)
}

fun Part1Function.runTest(expected: Any){
    runTest(1, expected)
}

fun Part2Function.runTest(expected: Any){
    runTest(2, expected)
}

private fun PartFunction.runTest(partNumber:Int, expected: Any){
    val testInputs = this.javaClass.readTestInputsPart(partNumber)
    val stopWatch = Stopwatch.createStarted()
    val result = this(testInputs)
    stopWatch.stop()

    check(result == expected) {
        """
            Part $partNumber test: Failed in ${stopWatch.elapsed().toKotlinDuration()}
            expected $expected but was $result
        """.trimIndent()}

    "Part 1 test: Succeeded in ${stopWatch.elapsed().toKotlinDuration()}".println()
}

private fun PartFunction.runPart(partNumber:Int) {
    val testInputs = this.javaClass.readInputs()
    val stopWatch = Stopwatch.createStarted()
    val result = this(testInputs)
    stopWatch.stop()
    """
        Part $partNumber: $result 
        Completed in ${stopWatch.elapsed().toKotlinDuration()}
    """.trimIndent().println()
}

/**
 * Reads lines from the given input txt file.
 * object{}.javaClass.readInputs()
 */
fun Class<*>.readInputs(): List<String> {
    return this.getResourceAsStream("inputs.txt")?.bufferedReader()?.readLines()
        ?: throw IllegalStateException("File not found")
}

fun Class<*>.readTestInputsPart(partNumber: Int): List<String> {
    return this.getResourceAsStream("test_inputs_part$partNumber.txt")?.bufferedReader()?.readLines()
        ?: throw IllegalStateException("File not found")
}