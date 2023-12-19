package commons.search

import java.util.*

object AStar {
    fun <T : Node<T>> search(start: T, heuristic: (T) -> Int, isGoal: (T) -> Boolean): Pair<Int, List<T>> {
        // Set of nodes already evaluated
        val closedSet: MutableSet<T> = HashSet()

        // Set of currently discovered nodes that are not evaluated yet.
        // Initially, only the start node is known.
        val openSet: PriorityQueue<T> = PriorityQueue()
        openSet.add(start)

        // For each node, which node it can most efficiently be reached from.
        // If a node can be reached from many nodes, cameFrom will eventually contain the
        // most efficient previous step.
        val cameFrom = mutableMapOf<T, T>()

        // For each node, the cost of getting from the start node to that node.
        val gScore = mutableMapOf<T, Int>()
        gScore[start] = 0

        // For each node, the total cost of getting from the start node to the goal
        // by passing by that node. That value is partly known, partly heuristic.
        start.withFScore(heuristic(start))

        while (openSet.isNotEmpty()) {
            val current: T = openSet.poll()

            if (isGoal(current)) {
                return Pair(gScore[current] ?: 0, reconstructPath(cameFrom, current))
            }

            closedSet.add(current)

            for (neighbor in current.getNeighbors()) {
                if (closedSet.contains(neighbor)) {
                    // Ignore the neighbor which is already evaluated.
                    continue
                }

                // The distance from start to a neighbor
                val tentativeGScore: Int = (gScore[current] ?: 0) + current.distanceTo(neighbor)

                if (tentativeGScore >= gScore.getOrDefault(neighbor, Int.MAX_VALUE)) {
                    // This is not a better path.
                    continue
                }

                // This path is the best until now. Record it!
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeGScore
                neighbor.withFScore(tentativeGScore + heuristic(neighbor))

                // Discover a new node
                openSet.add(neighbor)
            }
        }

        return Pair(-1, emptyList())
    }

    private fun <T : Node<T>> reconstructPath(cameFrom: Map<T, T>, current: T): List<T> {
        return generateSequence(current) { cameFrom[it] }.toList()
    }
}