package year2023.day17

import commons.*
import commons.Direction.*
import commons.Part.Companion.part1
import commons.search.AStar
import commons.search.Node

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 102) { heatLossMap ->
    val maxX = heatLossMap.keys.maxOf { it.x }
    val maxY = heatLossMap.keys.maxOf { it.y }

    val start = CrucibleNode(Coordinates(0, 0), emptyList(), heatLossMap, maxX, maxY)
    val goalCoordinates = Coordinates(heatLossMap.keys.maxOf { it.x }, heatLossMap.keys.maxOf { it.y })
    val goal = CrucibleNode(goalCoordinates, emptyList(), heatLossMap, maxX, maxY)
    AStar.search(start, { node -> node.distanceTo(goal) }, { maybeGoal -> maybeGoal.coordinates == goalCoordinates })
        .first
}

data class CrucibleNode(
    val coordinates: Coordinates,
    val last3Directions: List<Direction>,
    private val heatLossMap: HeatLossMap,
    private val maxX: Long,
    private val maxY: Long
) : Node<CrucibleNode>() {

    override fun getNeighbors(): Collection<CrucibleNode> {
        return listOf(
            coordinates.up() to UP,
            coordinates.down() to DOWN,
            coordinates.left() to LEFT,
            coordinates.right() to RIGHT
        )
            .asSequence()
            .filter { (coordinates, _) -> coordinates.x >= 0 && coordinates.y >= 0 && coordinates.x <= maxX && coordinates.y <= maxY }
            .filterNot { (_, direction) -> last3Directions.lastOrNull()?.opposite() == direction }
            .filterNot { (_, direction) -> get3LastSameDirections() == direction }
            .map { CrucibleNode(it.first, (last3Directions.takeLast(2) + it.second), heatLossMap, maxX, maxY) }
            .toList()
    }

    private fun get3LastSameDirections(): Direction? {
        return if (last3Directions.size == 3 && last3Directions.distinct().count() == 1) {
            last3Directions.first()
        } else {
            null
        }
    }

    val lastSameDirections = last3Directions.reversed().asSequence().groupNextToSame().firstOrNull() ?: emptyList()

    override fun distanceTo(other: CrucibleNode): Int {
        return heatLossMap[other.coordinates]!!.toInt() + this.coordinates.manhattanDistance(other.coordinates)
            .toInt() - 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CrucibleNode

        if (coordinates != other.coordinates) return false
        if (lastSameDirections != other.lastSameDirections) return false

        return true
    }

    override fun hashCode(): Int {
        return Pair(coordinates, lastSameDirections).hashCode()
    }
}