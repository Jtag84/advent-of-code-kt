package year2020.day17

import arrow.core.Tuple4
import com.google.common.collect.Sets
import commons.Coordinates4d
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 848) { initial2DSlice ->
    val initialActive4dCubes = initial2DSlice.filter { it.value == CubeState.ACTIVE }.keys.map { Coordinates4d(it.x, it.y, 0, 0) }.toSet()
    get6thCubeCycleActiveCount(initialActive4dCubes)
}