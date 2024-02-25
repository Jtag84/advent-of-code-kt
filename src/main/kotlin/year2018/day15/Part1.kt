package year2018.day15

import arrow.core.tail
import commons.*
import commons.Part.Companion.part1
import commons.search.AStar
import commons.search.Node

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 27730L) { cavernMap ->
    val maxX = cavernMap.maxOf { it.key.x }
    val elves = cavernMap.entries.filter { it.value == CavernMap.ELF }.mapIndexed { index, entry -> Elf(index, entry.key, 200) }
    val goblins = cavernMap.entries.filter { it.value == CavernMap.GOBLIN }.mapIndexed { index, entry -> Goblin(index, entry.key, 200) }

    combat(cavernMap, maxX, elves, goblins).first
}

fun combat(
    cavernMap: Map<Coordinates2d, CavernMap>,
    maxX: Long,
    elves: List<Elf>,
    goblins: List<Goblin>,
    hitBy : (Fighter) -> Long = {_ -> 3}
): Pair<Long, IndexedValue<Pair<List<Elf>, List<Goblin>>>> {
    var dontCountLastRound = false
    return generateSequence(elves to goblins) { currentElvesGoblins ->
        val (currentElves, currentGoblins) = currentElvesGoblins
        val fighterSequence = (currentElves + currentGoblins).asSequence()
            .sortedWith(Comparator.comparing({ it.coordinates }, readerOrderCoordinatesComparator(maxX)))

        val result = fighterSequence
            .foldUntil(
                currentElvesGoblins,
                { it.first.isEmpty() || it.second.isEmpty() },
                { acc, currentFighter ->
                    dontCountLastRound = currentFighter != fighterSequence.last()
                    executeCombatRule(cavernMap, maxX, acc, currentFighter, hitBy)
                })
        // debug print each position
//        val cavernMapToPrint = cavernMap.toMutableMap()
//        currentElves.forEach { cavernMapToPrint.put(it.coordinates, CavernMap.PRE_ELF) }
//        currentGoblins.forEach { cavernMapToPrint.put(it.coordinates, CavernMap.PRE_GOBLIN) }
//        result.first.forEach { cavernMapToPrint.put(it.coordinates, CavernMap.ELF) }
//        result.second.forEach { cavernMapToPrint.put(it.coordinates, CavernMap.GOBLIN) }
//        cavernMapToPrint.printMap()
//        result.first.sortedBy { it.id }.println()
//        result.second.sortedBy { it.id }.println()
//        "----------------".println()
        result
    }
    .withIndex()
    .dropWhile { fighters -> fighters.value.first.isNotEmpty() && fighters.value.second.isNotEmpty() }
    .first()
    .let {
        val roundOffset = if (dontCountLastRound) {
            -1
        } else {
            0
        }
        (roundOffset + it.index) * (it.value.first + it.value.second).sumOf { it.hitPoints } to it
    }
}

private fun readerOrderCoordinatesComparator(maxX: Long): Comparator<Coordinates2d> = Comparator.comparing { it.x + it.y * (maxX + 1) }

fun executeCombatRule(cavernMap: Map<Coordinates2d, CavernMap>,
                      maxX: Long,
                      allFighters: Pair<List<Elf>, List<Goblin>>,
                      currentFighter: Fighter,
                      hitBy : (Fighter) -> Long
) : Pair<List<Elf>, List<Goblin>> {
    if(allFighters.first.contains(currentFighter).not() && allFighters.second.contains(currentFighter).not() ) {
        return allFighters
    }

    val enemies = if(currentFighter is Goblin) {
            allFighters.first
        } else {
            allFighters.second
        }

    if(enemies.isEmpty()) {
        return allFighters
    }

    val allFightersSet = (allFighters.first.map { it.coordinates } + allFighters.second.map { it.coordinates }).toSet()
    var newAllFighters = allFighters

    val cardinals = currentFighter.coordinates.cardinals().toSet()
    var nextToEnnemies = enemies.filter {cardinals.contains(it.coordinates) }

    if(nextToEnnemies.isEmpty()) {
        val (_, path) = enemies
            .flatMap { it.coordinates.cardinals() }
            .filterNot { cavernMap[it]!! == CavernMap.WALL }
            .filterNot { allFightersSet.contains(it) }
            .map { calculatePath(cavernMap, maxX, allFightersSet, currentFighter.coordinates, it) }
            .filter { it.first > 0 }
            .sortedWith(
                Comparator.comparing { (distance, _) : Pair<Int, List<Coordinates2d>> -> distance }
                    .thenComparing ({it.second.first()}, readerOrderCoordinatesComparator(maxX))
                )
            .minByOrNull { it.first }
            ?: return allFighters

        // move near enemy
        if(path.isNotEmpty()) {
            val newCoordinates = path.dropLast(1).last()
            val newCardinals = newCoordinates.cardinals()
            nextToEnnemies = enemies.filter {newCardinals.contains(it.coordinates) }

            newAllFighters = when (currentFighter) {
                is Elf -> {
                    val currentFighterHitpoints = allFighters.first.first { currentFighter.id == it.id }.hitPoints
                    allFighters.first - currentFighter + Elf(currentFighter.id, newCoordinates, currentFighterHitpoints) to allFighters.second
                }
                is Goblin -> {
                    val currentFighterHitpoints = allFighters.second.first { currentFighter.id == it.id }.hitPoints
                    allFighters.first to allFighters.second - currentFighter + Goblin(currentFighter.id, newCoordinates, currentFighterHitpoints)
                }
                else -> throw IllegalStateException()
            }
        }
    }

    if(nextToEnnemies.isNotEmpty()) {
        // next to an enemy so attack enemy
        val closestEnemy = nextToEnnemies
            .sortedWith(Comparator.comparing<Fighter?, Long?> { it.hitPoints }
                .thenComparing({it.coordinates}, readerOrderCoordinatesComparator(maxX)))
            .first()

        val newClosestEnemy = closestEnemy
            .minusHitPoints(hitBy(currentFighter))
            .takeIf { it.hitPoints > 0 }

        newAllFighters = when (closestEnemy) {
            is Elf      -> {
                check(newClosestEnemy is Elf?)
                (newClosestEnemy?.let { listOf(it) }?: emptyList()) + (newAllFighters.first - closestEnemy) to newAllFighters.second
            }
            is Goblin   -> {
                check(newClosestEnemy is Goblin?)
                newAllFighters.first to (newClosestEnemy?.let { listOf(it) }?: emptyList()) + (newAllFighters.second - closestEnemy)
            }
            else -> throw IllegalStateException()
        }
    }

    return newAllFighters
}

val cache = hashMapOf<Triple<Set<Coordinates2d>, Coordinates2d, Coordinates2d>, Pair<Int, List<Coordinates2d>>>()
fun calculatePath(cavernMap: Map<Coordinates2d, CavernMap>, maxX: Long, allFighters: Set<Coordinates2d>, currentFighter: Coordinates2d, enemy: Coordinates2d) : Pair<Int, List<Coordinates2d>> {
    return cache.getOrPut(Triple(allFighters, currentFighter, enemy)) {
        val node = CavernNode(cavernMap, maxX, allFighters, currentFighter)
        AStar.search(
            node,
//            {it.coordinates.manhattanDistance(enemy).toInt()}, // this heuristic only works for part 1, due to some priority path constraint and the inputs, this doesn't work
            {1}, // setting the heuristic to 1 should be equivalent to djikstra in the end
            {it.coordinates == enemy},
            {cameFrom, currentNode, neighbor ->
                val currentNodePath = AStar.reconstructPath(cameFrom, currentNode).reversed()
                val neighborPath = AStar.reconstructPath(cameFrom, neighbor).tail().reversed()
                readerOrderCoordinatesComparator(maxX).compare(neighborPath[1].coordinates, currentNodePath[1].coordinates) > 0
            }
        )
        .let { it.first to it.second.map { it.coordinates } }
    }
}

data class CavernNode(val cavernMap: Map<Coordinates2d, CavernMap>, val maxX: Long, val allFighters: Set<Coordinates2d>, val coordinates: Coordinates2d) : Node<CavernNode>() {
    override fun getNeighbors(): Collection<CavernNode> {
        return this.coordinates.cardinals()
            .filterNot { cavernMap[it]!! == CavernMap.WALL }
            .filterNot { allFighters.map { it }.contains(it) }
            .sortedWith(readerOrderCoordinatesComparator(maxX))
            .map { CavernNode(cavernMap, maxX, allFighters, it) }
            .toSet()
    }

    override fun compareTo(other: Node<*>): Int {
        other as CavernNode
        val fscoreComparison = super.fScore.compareTo(other.fScore)
        if(fscoreComparison == 0) {
            return readerOrderCoordinatesComparator(maxX).compare(this.coordinates, other.coordinates)
        }
        return fscoreComparison
    }

    override fun distanceTo(other: CavernNode): Int {
        return 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CavernNode

        return coordinates == other.coordinates
    }

    override fun hashCode(): Int {
        return (this.coordinates.x + this.coordinates.y * (maxX + 1)).toInt()
    }

    override fun toString(): String {
        return "CavernNode(coordinates=$coordinates)"
    }
}

abstract class Fighter(open val id: Int, open val coordinates: Coordinates2d, open val hitPoints: Long) {
    abstract fun minusHitPoints(points: Long) : Fighter
}

data class Elf(override val id: Int, override val coordinates: Coordinates2d, override val hitPoints: Long) : Fighter(id, coordinates, hitPoints) {
    override fun minusHitPoints(points: Long): Elf {
        return Elf(id, coordinates, hitPoints - points)
    }

    override fun toString(): String {
        return "E($id, $coordinates, $hitPoints)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Elf) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

data class Goblin(override val id: Int, override val coordinates: Coordinates2d, override val hitPoints: Long) : Fighter(id, coordinates, hitPoints) {
    override fun minusHitPoints(points: Long): Goblin {
        return Goblin(id, coordinates, hitPoints - points)
    }

    override fun toString(): String {
        return "G($id, $coordinates, $hitPoints)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Goblin) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}