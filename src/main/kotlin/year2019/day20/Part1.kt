package year2019.day20

import commons.Coordinates
import commons.Coordinates2d
import commons.Part.Companion.part1
import commons.cardinals
import commons.manhattanDistance
import commons.search.AStar
import commons.search.Node

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}
val part1 = part1(inputParser, 23) { mazeMap->
    val start = mazeMap.entries.filter { it.value is MazeElement.Start }.first().key
    val finish = mazeMap.entries.filter { it.value is MazeElement.Finish }.first().key

    AStar.search(
        MazeNode(mazeMap, start),
        {1}, // fast enough without a complicated heuristic, the portal makes it harder to find a consistent one anyway
        {it.coordinates == finish}
    ).first
}

data class MazeNode(val mazeMap: MazeMap, val coordinates: Coordinates2d) : Node<MazeNode>() {
    override fun getNeighbors(): Collection<MazeNode> {
        return coordinates.cardinals().mapNotNull { cardinalCoordinates ->
            mazeMap[cardinalCoordinates]?.let {
                when(it) {
                    MazeElement.Finish, MazeElement.Empy -> MazeNode(mazeMap, cardinalCoordinates)
                    is MazeElement.InnerPortal -> MazeNode(mazeMap, it.toCoordinates)
                    is MazeElement.OuterPortal -> MazeNode(mazeMap, it.toCoordinates)
                    MazeElement.Wall, MazeElement.Start -> null
                    is MazeElement.PartialPortal, MazeElement.Outside -> throw IllegalStateException()
                }
            }
        }
    }

    override fun distanceTo(other: MazeNode): Int {
        return 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MazeNode

        return coordinates == other.coordinates
    }

    override fun hashCode(): Int {
        return coordinates.hashCode()
    }

    override fun toString(): String {
        return "MazeNode(coordinates=$coordinates)"
    }
}