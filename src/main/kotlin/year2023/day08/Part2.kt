package year2023.day08

import Part.Companion.part2
import org.apache.commons.math3.primes.Primes.primeFactors

fun main() {
    part2.runTest()
    part2.run()
}

val part2 = part2(inputParser, 6L) { (instructions, nodeMap) ->
    val startingNodes = nodeMap.keys.filter { it.endsWith("A") }

    /**
     * When iterating through the count between each node ending in Z, you will notice that the count increments by the same amount each time,
     * so you only need to calculate the count for the first ending node of each starting node.
     * Then you get the prime factors of all and multiply them together to get the first count where they will all line up on an ending node
     */
    startingNodes
        .asSequence()
        .map { countToNextNodeEndingInZ(instructions, nodeMap, it, "Z") }
        .flatMap { primeFactors(it) }
        .map { it.toLong() }
        .distinct()
        .reduce(Math::multiplyExact)
}
