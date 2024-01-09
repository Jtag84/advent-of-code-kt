package year2023.day17

import commons.*
import commons.Part.Companion.part2
import commons.search.AStar
import commons.search.Node

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 71) { heatLossMap ->
    val maxX = heatLossMap.keys.maxOf { it.x }
    val maxY = heatLossMap.keys.maxOf { it.y }
    val start = UltraCrucibleNode(Coordinates2d(0, 0), emptyList(), heatLossMap, maxX, maxY)
    val goalCoordinates = Coordinates2d(heatLossMap.keys.maxOf { it.x }, heatLossMap.keys.maxOf { it.y })
    val goal = UltraCrucibleNode(goalCoordinates, emptyList(), heatLossMap, maxX, maxY)
    AStar.search(
        start,
        { node -> node.distanceTo(goal) },
        { maybeGoal -> maybeGoal.coordinates == goalCoordinates && maybeGoal.lastSameDirections.count() >= 4 })
        .first
}

data class UltraCrucibleNode(
    val coordinates: Coordinates2d,
    val last10Directions: List<Direction>,
    private val heatLossMap: HeatLossMap,
    private val maxX: Long,
    private val maxY: Long
) : Node<UltraCrucibleNode>() {

    override fun getNeighbors(): Collection<UltraCrucibleNode> {

        return listOf(
            coordinates.up() to Direction.UP,
            coordinates.down() to Direction.DOWN,
            coordinates.left() to Direction.LEFT,
            coordinates.right() to Direction.RIGHT
        )
            .asSequence()
            .filter { (coordinates, _) -> isWithinBounds(coordinates) }
            .filter { (_, direction) ->
                lastSameDirections.count() >= 4 || (lastSameDirections.firstOrNull()?.let { it == direction } ?: true)
            }
            .filterNot { (_, direction) -> last10Directions.lastOrNull()?.opposite() == direction }
            .filterNot { (_, direction) -> getLastNInSameDirections(10) == direction }
            .map { UltraCrucibleNode(it.first, last10Directions.takeLast(9) + it.second, heatLossMap, maxX, maxY) }
            .toList()
    }

    private fun isWithinBounds(coordinates: Coordinates2d) =
        coordinates.x >= 0 && coordinates.y >= 0 && coordinates.x <= maxX && coordinates.y <= maxY

    private fun getLastNInSameDirections(n: Int): Direction? {
        return if (last10Directions.size == n && last10Directions.distinct().count() == 1) {
            last10Directions.first()
        } else {
            null
        }
    }

    val lastSameDirections = last10Directions.reversed().asSequence().groupNextToSame().firstOrNull() ?: emptyList()

    override fun distanceTo(other: UltraCrucibleNode): Int {
        return heatLossMap[other.coordinates]!!.toInt() + this.coordinates.manhattanDistance(other.coordinates)
            .toInt() - 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UltraCrucibleNode

        if (coordinates != other.coordinates) return false
        if (lastSameDirections != other.lastSameDirections) return false

        return true
    }

    override fun hashCode(): Int {
        return Pair(coordinates, lastSameDirections).hashCode()
    }
}