package year2020.day17

import com.google.common.collect.Sets
import commons.Coordinates3d
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 112) { initial2DSlice ->
    val initialActive3dCubes = initial2DSlice.filter { it.value == CubeState.ACTIVE }.keys.map { Coordinates3d(it.x, it.y, 0) }.toSet()
    val neighborMap = mutableMapOf<Coordinates3d, Set<Coordinates3d>>()
    generateSequence(initialActive3dCubes) {active3dCubes ->
            val inactiveCubes = active3dCubes.flatMap { neighborMap.getNeighbors(it) }.toSet() - active3dCubes
            val newlyActiveCubes = inactiveCubes.filter {
                    neighborMap.getNeighbors(it).count {active3dCubes.contains(it)} == 3
                }.toSet()

            val newlyInactiveCubes = active3dCubes.filterNot {
                    neighborMap.getNeighbors(it).count {active3dCubes.contains(it)} in 2..3
                }.toSet()

            active3dCubes - newlyInactiveCubes + newlyActiveCubes
        }
        .drop(1)
        .take(6)
        .last()
        .count()
}

fun MutableMap<Coordinates3d, Set<Coordinates3d>>.getNeighbors(coordinates3d: Coordinates3d): Set<Coordinates3d> {
    return this.computeIfAbsent(coordinates3d, Coordinates3d::getAllAroundNeighbors)
}