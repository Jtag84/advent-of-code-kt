package year2020.day17

import arrow.core.Tuple4
import com.google.common.collect.Sets
import commons.Coordinates3d
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

typealias Coordinate4d = Tuple4<Long, Long, Long, Long>

val part2 = part2(inputParser, 848) { initial2DSlice ->
    val initialActive4dCubes = initial2DSlice.filter { it.value == CubeState.ACTIVE }.keys.map { Coordinate4d(it.x, it.y, 0, 0) }.toSet()
    val neighborMap = mutableMapOf<Coordinate4d, Set<Coordinate4d>>()
    generateSequence(initialActive4dCubes) {active4dCubes ->
        val inactiveCubes = active4dCubes.flatMap { neighborMap.getNeighbors(it) }.toSet() - active4dCubes
        val newlyActiveCubes = inactiveCubes.filter {
            neighborMap.getNeighbors(it).count {active4dCubes.contains(it)} == 3
        }.toSet()

        val newlyInactiveCubes = active4dCubes.filterNot {
            neighborMap.getNeighbors(it).count {active4dCubes.contains(it)} in 2..3
        }.toSet()

        active4dCubes - newlyInactiveCubes + newlyActiveCubes
    }
        .drop(1)
        .take(6)
        .last()
        .count()
}

fun MutableMap<Coordinate4d, Set<Coordinate4d>>.getNeighbors(coordinates4d: Coordinate4d): Set<Coordinate4d> {
    return this.computeIfAbsent(coordinates4d, Coordinate4d::getAllAroundNeighbors)
}

fun Coordinate4d.getAllAroundNeighbors(): Set<Coordinate4d> {
    return Sets.cartesianProduct(setOf(first-1, first, first+1), setOf(second-1, second, second+1), setOf(third-1, third, third+1), setOf(fourth-1, fourth, fourth+1))
        .map { (x, y, z, w) -> Coordinate4d(x, y, z, w) }
        .toSet() - this
}