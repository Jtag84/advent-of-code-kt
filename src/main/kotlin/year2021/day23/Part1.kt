package year2021.day23

import com.google.common.collect.Sets
import commons.*
import commons.Part.Companion.part1
import commons.search.AStar
import commons.search.Node
import year2021.day23.BurrowMapElement.*
import kotlin.math.abs

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 12521) { (burrowMap, amphipods) ->
    val startNode = AmphipodsStateNode(burrowMap, 2, amphipods)

    val goal = setOf(
        Coordinates(x=3, y=2) to AMBER_AMPHIPOD,   Coordinates(x=5, y=2) to BRONZE_AMPHIPOD,   Coordinates(x=7, y=2) to COPPER_AMPHIPOD, Coordinates(x=9, y=2) to DESERT_AMPHIPOD,
        Coordinates(x=3, y=3) to AMBER_AMPHIPOD,   Coordinates(x=5, y=3) to BRONZE_AMPHIPOD,   Coordinates(x=7, y=3) to COPPER_AMPHIPOD, Coordinates(x=9, y=3) to DESERT_AMPHIPOD
    )

    val goalByType = goal.groupBy( { it.second } , {it.first}).mapValues { it.value.toSet() }

    val (cost, path) = AStar.search(startNode, {it.heuristic(goalByType)}, {it.amphipods == goal})

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

val outsideRoomCoordinates = setOf(Coordinates(3, 1), Coordinates(5, 1), Coordinates(7, 1), Coordinates(9, 1))

data class AmphipodsStateNode(val burrowMap: BurrowMap, val roomSize: Int, val amphipods: Amphipods) : Node<AmphipodsStateNode>() {
    val amphipodCoordinates = amphipods.map { it.first }.toSet()

    override fun getNeighbors(): Collection<AmphipodsStateNode> {
        return this.amphipods.map { (coordinates, type) ->
            val accessibleCoordinates = getAccessibleCoordinates(type, coordinates)
            (coordinates to type) to accessibleCoordinates
        }
        .filter{it.second.isNotEmpty()}
        .flatMap { (amphipodToChange, neighbors) ->
            neighbors.map {
                amphipods
                    .map { amphipod ->
                        if (amphipod == amphipodToChange) {
                            it to amphipod.second
                        } else {
                            amphipod
                        }
                    }.toSet()
            }
        }
        .map { AmphipodsStateNode(burrowMap, roomSize, it) }
    }

    private fun isFinalRoomOccupiedByDifferentType(type: BurrowMapElement) : Boolean {
        return when (type){
            OUTSIDE ,WALL, EMPTY_SPACE -> throw  IllegalStateException()
            else -> amphipods.any { it.second != type && it.first.y > 1 && it.first.x == getFinalRoomX(type) }
        }
    }

    private fun getFinalRoomX(type: BurrowMapElement) : Long {
        return when (type){
            OUTSIDE ,WALL, EMPTY_SPACE -> throw  IllegalStateException()
            AMBER_AMPHIPOD -> 3L
            BRONZE_AMPHIPOD -> 5L
            COPPER_AMPHIPOD -> 7L
            DESERT_AMPHIPOD -> 9L
        }
    }


    private fun Coordinates.isInFinalRoom(type: BurrowMapElement) : Boolean {
        return when (type){
            OUTSIDE ,WALL, EMPTY_SPACE -> throw  IllegalStateException()
            AMBER_AMPHIPOD -> y > 1 && x == 3L
            BRONZE_AMPHIPOD -> y > 1 && x == 5L
            COPPER_AMPHIPOD -> y > 1 && x == 7L
            DESERT_AMPHIPOD -> y > 1 && x == 9L
        }
    }

    private fun Coordinates.isInHallway() : Boolean {
        return (this.y == 1L)
    }

    fun getAccessibleCoordinates(type: BurrowMapElement, from: Coordinates) : Set<Coordinates> {
        return when {
            from.isInHallway() && isFinalRoomOccupiedByDifferentType(type).not() -> {
                getAvailableFinalRoomCoordinates(type, from).toSet()
            }
            from.isInHallway().not() -> {
                   val isHallwayAccessible =
                       generateSequence ( from.up() ) {it.up()}.takeWhile { it.y > 1 }.none { amphipodCoordinates.contains(it) }
                if(isHallwayAccessible) {
                    val leftHallway = generateSequence(Coordinates(from.x, 1)) {it.left()}
                        .drop(1)
                        .takeWhile { it.x > 0 && amphipodCoordinates.contains(it).not() }
                        .filterNot { outsideRoomCoordinates.contains(it)}
                    val rightHallway = generateSequence(Coordinates(from.x, 1)) {it.right()}
                        .drop(1)
                        .takeWhile { it.x < 12 && amphipodCoordinates.contains(it).not() }
                        .filterNot { outsideRoomCoordinates.contains(it)}

                    val availableFinalRoomCoordinates = if(isFinalRoomOccupiedByDifferentType(type)) {
                        emptySequence()
                    }
                    else {
                        getAvailableFinalRoomCoordinates(type, from)
                    }

                    (leftHallway + rightHallway + availableFinalRoomCoordinates).toSet()
                }
                else {
                    emptySet()
                }
            }
            else -> emptySet()
        }
    }

    private fun getAvailableFinalRoomCoordinates(
        type: BurrowMapElement,
        from: Coordinates
    ): Sequence<Coordinates> {
        val finalRoomX = getFinalRoomX(type)
        val step = if (finalRoomX > from.x) {
            Coordinates::right
        } else {
            Coordinates::left
        }
        val isFinalRoomAccessible = from.isInFinalRoom(type) || generateSequence(step(Coordinates(from.x, 1), 1)) { coordinates -> step(coordinates, 1) }
            .takeWhile { it.x != finalRoomX }
            .none { amphipodCoordinates.contains(it) }

        return if (isFinalRoomAccessible) {
            generateSequence(Coordinates(finalRoomX, 2)) {it.down()}.take(roomSize)
                .takeWhile {amphipodCoordinates.contains(it).not() }
        } else {
            emptySequence()
        }
    }

    override fun distanceTo(other: AmphipodsStateNode): Int {
        val otherByType = other.amphipods.groupBy( { it.second } , {it.first}).mapValues { it.value.toSet() }

        return this.amphipods.distanceTo(otherByType)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AmphipodsStateNode

        return amphipods == other.amphipods
    }

    override fun hashCode(): Int {
        return amphipods.toList().sortedBy { it.first }.fold(7) {acc, element -> acc * 31 + element.hashCode()}
    }
}

fun Amphipods.distanceTo(otherByType: Map<BurrowMapElement, Set<Coordinates>>): Int {
    val totalManhattanDistance = this.groupBy( { it.second } , {it.first})
        .mapValues {
            val difference = Sets.symmetricDifference(it.value.toSet(), otherByType[it.key]!!)

            if(difference.isEmpty()) {
                0
            }
            else {
                check(difference.size == 2)

                it.key.getCost() * calculateTotalDistance(difference.first(), difference.last())
            }
        }
        .values.sum()

    return totalManhattanDistance.toInt()
}

private fun calculateTotalDistance (left: Coordinates, right: Coordinates) : Long {
    return when {
        left.x != right.x -> abs(left.x - right.x) + left.y - 1 + right.y -1
        else -> abs(left.y - right.y)
    }
}

fun AmphipodsStateNode.heuristic(goalByType: Map<BurrowMapElement, Set<Coordinates>>): Int {
    val groupedByType = this.amphipods.groupBy( { it.second } , {it.first}).mapValues { it.value.toSet() }
    return goalByType.entries
        .sumOf {
            val nonGoalAmphipods = groupedByType[it.key]!! - it.value
            val nonAmphipodsGoal = it.value - groupedByType[it.key]!!

            check(nonAmphipodsGoal.size == nonGoalAmphipods.size)
            it.key.getCost() * nonGoalAmphipods.zip(nonAmphipodsGoal).sumOf { (left, right) -> calculateTotalDistance(left, right) }
        }.toInt()
}