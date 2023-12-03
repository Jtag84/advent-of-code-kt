import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.ParserResult
import cc.ekblad.konbini.parse
import com.google.common.base.Stopwatch
import kotlin.time.toKotlinDuration

fun interface PartFunction {
    operator fun invoke(inputs : String): Any
}

fun interface Part1Function : PartFunction
fun interface Part2Function : PartFunction

fun <T> Parser<T>.parseOrThrowException(input:String) : T {
    val parseResult = this.parse(input)
    when(parseResult) {
        is ParserResult.Ok -> return parseResult.result
        else -> throw IllegalStateException(parseResult.toString())
    }
}

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

    "Part $partNumber test: Succeeded in ${stopWatch.elapsed().toKotlinDuration()}".println()
}

private fun PartFunction.runPart(partNumber:Int) {
    val inputs = this.javaClass.readInputs()
    val stopWatch = Stopwatch.createStarted()
    val result = this(inputs)
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
fun Class<*>.readInputs(): String {
    return this.getResourceAsStream("inputs.txt")?.bufferedReader()?.readText()
        ?: throw IllegalStateException("File not found")
}

fun Class<*>.readTestInputsPart(partNumber: Int): String {
    return this.getResourceAsStream("test_inputs_part$partNumber.txt")?.bufferedReader()?.readText()
        ?: throw IllegalStateException("File not found")
}