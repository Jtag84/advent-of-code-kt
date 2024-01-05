package year2021.day19

import com.google.common.collect.Sets
import commons.Part.Companion.part2
import commons.manhattanDistance

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 3621L) { scanners ->
    val (scannerCoordinates, _) = memoizedGetAdjustedCoordinates(scanners)

    Sets.combinations(scannerCoordinates, 2).map { scannerCombinations -> scannerCombinations.first().manhattanDistance(scannerCombinations.last()) }.max()
}