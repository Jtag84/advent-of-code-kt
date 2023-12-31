package year2021.day23

import commons.Coordinates
import commons.Part.Companion.part2
import commons.println
import commons.search.AStar
import commons.toStringMap

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParserPart2, 44169) { (burrowMap, amphipods) ->
    val startNode = AmphipodsStateNode(burrowMap, 4, amphipods)

    val goalPart2 = setOf(
        Coordinates(x=3, y=2) to BurrowMapElement.AMBER_AMPHIPOD,   Coordinates(x=5, y=2) to BurrowMapElement.BRONZE_AMPHIPOD,   Coordinates(x=7, y=2) to BurrowMapElement.COPPER_AMPHIPOD, Coordinates(x=9, y=2) to BurrowMapElement.DESERT_AMPHIPOD,
        Coordinates(x=3, y=3) to BurrowMapElement.AMBER_AMPHIPOD,   Coordinates(x=5, y=3) to BurrowMapElement.BRONZE_AMPHIPOD,   Coordinates(x=7, y=3) to BurrowMapElement.COPPER_AMPHIPOD, Coordinates(x=9, y=3) to BurrowMapElement.DESERT_AMPHIPOD,
        Coordinates(x=3, y=4) to BurrowMapElement.AMBER_AMPHIPOD,   Coordinates(x=5, y=4) to BurrowMapElement.BRONZE_AMPHIPOD,   Coordinates(x=7, y=4) to BurrowMapElement.COPPER_AMPHIPOD, Coordinates(x=9, y=4) to BurrowMapElement.DESERT_AMPHIPOD,
        Coordinates(x=3, y=5) to BurrowMapElement.AMBER_AMPHIPOD,   Coordinates(x=5, y=5) to BurrowMapElement.BRONZE_AMPHIPOD,   Coordinates(x=7, y=5) to BurrowMapElement.COPPER_AMPHIPOD, Coordinates(x=9, y=5) to BurrowMapElement.DESERT_AMPHIPOD
    )

    val goalByType = goalPart2.groupBy( { it.second } , {it.first}).mapValues { it.value.toSet() }

    val (cost, path) = AStar.search(startNode, {it.heuristic(goalByType)}, {it.amphipods == goalPart2})
    // uncomment to print out each step
//    path.reversed().forEach {
//        val map = burrowMap.toMutableMap()
//        it.amphipods.forEach { (coordinates, type) ->
//            map.put(coordinates, type)
//        }
//        map.toStringMap().println()
//        "".println()
//    }

    cost
}