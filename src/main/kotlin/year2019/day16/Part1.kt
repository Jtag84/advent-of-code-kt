package year2019.day16

import commons.Part.Companion.part1
import commons.repeat
import kotlin.math.abs

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, "24176176") { signal ->
    generateSequence(signal) {it.fftPhase()}.drop(100).first().take(8).joinToString("")
}

fun List<Int>.fftPhase() : List<Int> {
    return this.mapIndexed { index, _ ->
        this.asSequence()
            .zip(patternForIndex(index), Math::multiplyExact)
            .sum().let { abs(it) % 10 }
    }
}

val patternCache = hashMapOf<Int, Sequence<Int>>()
fun patternForIndex(index: Int) : Sequence<Int> = patternCache.getOrPut(index) {
    val start = sequenceOf(0, 1, 0, -1)
    return start.flatMap { sequenceOf(it).repeat().take(index + 1) }.repeat().drop(1)
}