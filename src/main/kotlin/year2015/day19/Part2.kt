package year2015.day19

import commons.Part.Companion.part2
import commons.search.AStar
import commons.search.Node
import year2021.day23.AmphipodsStateNode
import kotlin.math.abs

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

// inputs are of the form:
// X => X Rn X Ar | X Rn X Y X Ar | X Rn X Y X Y X Ar
// Rn Ar and Y are finals
// So -> total size - Rn Ar count - 2 * y count - 1
val part2 = part2(inputParser, null) { (_, moleculeChain) ->
    val rnArCount = moleculeChain.count { it == "Rn" || it == "Ar" }
    val yCount = moleculeChain.count{it == "Y"}
    moleculeChain.size - rnArCount - 2 * yCount - 1
}