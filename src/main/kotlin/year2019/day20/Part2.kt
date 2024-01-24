package year2019.day20

import commons.Coordinates2d
import commons.Part.Companion.part2
import commons.cardinals
import commons.search.AStar
import commons.search.Node

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 396) { mazeMap->
    val start = mazeMap.entries.filter { it.value is MazeElement.Start }.first().key
    val finish = mazeMap.entries.filter { it.value is MazeElement.Finish }.first().key

    AStar.search(
        MazeLevelNode(mazeMap, start, 0),
        {1}, // fast enough without a complicated heuristic, the portal makes it harder to find a consistent one anyway
        {it.level == 0 && it.coordinates == finish}
    ).first
}

data class MazeLevelNode(val mazeMap: MazeMap, val coordinates: Coordinates2d, val level:Int) : Node<MazeLevelNode>() {
    override fun getNeighbors(): Collection<MazeLevelNode> {
        return coordinates.cardinals().mapNotNull { cardinalCoordinates ->
            mazeMap[cardinalCoordinates]?.let {
                when(it) {
                    MazeElement.Start, MazeElement.Finish, MazeElement.Empy -> MazeLevelNode(mazeMap, cardinalCoordinates, level)
                    is MazeElement.InnerPortal -> MazeLevelNode(mazeMap, it.toCoordinates, level+1)
                    is MazeElement.OuterPortal -> {
                        if(level == 0) {
                            null
                        } else {
                            MazeLevelNode(mazeMap, it.toCoordinates, level-1)
                        }
                    }
                    MazeElement.Wall -> null
                    is MazeElement.PartialPortal, MazeElement.Outside -> throw IllegalStateException()
                }
            }
        }
    }

    override fun distanceTo(other: MazeLevelNode): Int {
        return 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MazeLevelNode

        if (coordinates != other.coordinates) return false
        if (level != other.level) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coordinates.hashCode()
        result = 31 * result + level
        return result
    }

    override fun toString(): String {
        return "MazeLevelNode(coordinates=$coordinates, level=$level)"
    }
}