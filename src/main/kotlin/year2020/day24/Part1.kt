package year2020.day24

import commons.*
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 10) { tileDirections ->
    getBlackTiles(tileDirections).count()
}

fun getBlackTiles(tileDirections : List<List<TileDirection>>): Set<HexagonGridCoordinates> {
    val startCoordinates = HexagonGridCoordinates(1000000,1000000)
    return tileDirections.map { it.fold(startCoordinates) {acc, tileDirection ->  tileDirection.move(acc)} }
        .groupBy { it }
        .mapValues { it.value.size }
        .filter { it.value.isOdd() }
        .keys
}