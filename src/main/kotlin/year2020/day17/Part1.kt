package year2020.day17

import commons.Coordinates
import commons.Coordinates3d
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 112) { initial2DSlice ->
    val initialActive3dCubes = initial2DSlice.filter { it.value == CubeState.ACTIVE }.keys.map { Coordinates3d(it.x, it.y, 0) }.toSet()
    get6thCubeCycleActiveCount(initialActive3dCubes)
}

fun <T : Coordinates<T>>  get6thCubeCycleActiveCount(initialActiveCubes: Set<T>): Int {
    val neighborMap = mutableMapOf<T, Set<T>>()
    return generateSequence(initialActiveCubes) { activeCubes ->
            val inactiveCubes = activeCubes.flatMap { neighborMap.getNeighbors(it) }.toSet() - activeCubes
            val newlyActiveCubes = inactiveCubes.filter {
                neighborMap.getNeighbors(it).count { activeCubes.contains(it) } == 3
            }.toSet()

            val newlyInactiveCubes = activeCubes.filterNot {
                neighborMap.getNeighbors(it).count { activeCubes.contains(it) } in 2..3
            }.toSet()

            activeCubes - newlyInactiveCubes + newlyActiveCubes
        }
        .drop(1)
        .take(6)
        .last()
        .count()
}

private fun <T : Coordinates<T>> MutableMap<T, Set<T>>.getNeighbors(coordinates: T): Set<T> {
    return this.computeIfAbsent(coordinates) {it.allAround()}
}