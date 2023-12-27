package year2023.day25

import com.google.common.collect.Sets
import commons.*
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

private const val SAMPLE_PATHS = 2_000 // increase that number if no solution is found
private const val NUMBER_OF_WIRES_TO_CUT = 3

val part1 = part1(inputParser, 54) { graph ->

    // statistical approach, takes 2 point and trace the path, branches are chosen randomly,
    // only keeps long path, the most traveled edges must be the one to cut.
    // this approach might not give the right answer every time. The higher the sample the higher the
    // chances to get the right answer but the longer it will take. Note that this scales linearily
    val wiresToCut = Sets.combinations(graph.keys, 2).map { it.first() to it.last() }
        .asSequence().repeat()
        .take(SAMPLE_PATHS)
        .map { buildPath(graph.toMutableMap(), listOf(it.first), it.second)}
        .filter { it.size >= graph.keys.size / 4 } // only keep long paths (greatest performance improvement)
        .flatMap { it.zipWithNext() }
        .groupBy { minOf(it.first, it.second) to maxOf(it.first, it.second) }
        .mapValues { it.value.size }
        .entries
        .sortedBy { it.value }
        .reversed()
        .take(NUMBER_OF_WIRES_TO_CUT)
        .map { it.key.toList().toSet() }

    val wiresCutGraph = graph.toMutableMap().mapValues {
            wiresToCut.firstOrNull { wire -> wire.contains(it.key) }?.let {
                wire ->
                it.value.filterNot (wire::contains).toSet()
        }
        ?: it.value
    }.toMutableMap()

    val group1 = groupNodes(wiresCutGraph, wiresCutGraph.keys.first())
    if(wiresCutGraph.isEmpty()) {
        "can't find another group try increasing SAMPLE_PATHS ($SAMPLE_PATHS) ".toRedString()
    } else {
        val group2 = groupNodes(wiresCutGraph, wiresCutGraph.keys.first())
        group1.size * group2.size
    }
}

fun groupNodes(graph: MutableMap<String, Set<String>>, nodeToTravel: String) : Set<String> {
    if(graph.isEmpty()) {
        return setOf(nodeToTravel)
    }

    val nextNodes = graph.remove(nodeToTravel)

    if(nextNodes == null) {
        return setOf(nodeToTravel)
    }
    else {
        return nextNodes.flatMap { groupNodes(graph, it) }.toSet()
    }
}

tailrec fun buildPath(graph: MutableMap<String, Set<String>>,currentPath: List<String>, goal: String) : List<String> {
    val currentNode = currentPath.last()
    return when {
        currentNode == goal -> currentPath
        graph.isEmpty() -> currentPath // no valid path from the randomly chosen branch
        else -> {
            val next = graph.remove(currentNode)
                ?.shuffled() // randomly chose a branch
                ?.filter { graph.keys.contains(it) }
                ?.firstOrNull()

            if (next == null) {
                return currentPath
            } else {
                return buildPath(graph, currentPath + next, goal)
            }
        }
    }
}