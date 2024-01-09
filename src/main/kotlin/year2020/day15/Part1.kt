package year2020.day15

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 436L) { startingNumbers ->
    getNthSpokenNumber(startingNumbers, 2020)
}

fun getNthSpokenNumber(startingNumbers: List<Long>, n: Int): Long {
    val memory =
        startingNumbers.mapIndexed { index, number -> number to index.toLong() }.dropLast(1).toMap()
            .toMutableMap()

    return generateSequence(
                Pair(
                    startingNumbers.last(),
                    startingNumbers.size.toLong() - 1
                )
            ) { (lastNumber, index) ->
                val valueToReturn = if (memory.contains(lastNumber)) {
                    index - memory[lastNumber]!!
                } else {
                    0L
                }

                memory[lastNumber]= index
                Pair(valueToReturn, index + 1)
            }
        .drop(n - startingNumbers.size )
        .first()
        .first
}