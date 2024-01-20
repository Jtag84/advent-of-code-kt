package year2019.day16

import commons.Part.Companion.part2
import commons.repeat

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, "84462026") { signal ->
    val messageOffset = signal.take(7).joinToString("").toInt()
    val offsetFromEnd = signal.size * 10000 - messageOffset

    val newSignalFromEnd = signal.reversed().asSequence().repeat().take(offsetFromEnd).toList()
    generateSequence(newSignalFromEnd) {it.fftPhasePart2()}
        .drop(100)
        .first()
        .reversed()
        .take(8)
        .joinToString("")
}

// newPhase[i] = newPhase[i+1] + previousPhase[i]
// So starting from the end in reverse we don't have to caclulate everything.
// Note that due to the pattern, this will not compute the first half of the signal, so this is not valid for part1
fun List<Int>.fftPhasePart2() : List<Int> {
    var previousValue = 0
    return this.map {
            val newPhaseValue = (it + previousValue) % 10
            previousValue = newPhaseValue
            newPhaseValue
        }
}