package year2020.day20

import arrow.core.tail
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 20899048083289L) { tiles ->
    val connectedTiles = getConnectedTiles(tiles)

    connectedTiles
        .filter { listOf(it.topConnectedTo, it.rightConnectedTo, it.bottomConnectedTo, it.leftConnectedTo).filterNotNull().count() == 2 }
        .map { it.id }
        .reduce(Math::multiplyExact)
}

fun getConnectedTiles(tiles: List<Tile>): MutableList<Tile> {
    val connectedTiles = mutableListOf(tiles.first())
    val remainingTiles = tiles.tail().toMutableList()

    while (remainingTiles.isNotEmpty()) {
        val inspectingTile = remainingTiles.removeFirst()

        connectedTiles.map { connectedTile ->
            when {
                connectedTile.topConnectedTo == null
                        && inspectingTile.bottomConnectedTo == null
                        && connectedTile.top == inspectingTile.bottom.reversed() -> {
                    connectedTile.topConnectedTo = inspectingTile
                    inspectingTile.bottomConnectedTo = connectedTile
                }

                connectedTile.rightConnectedTo == null
                        && inspectingTile.leftConnectedTo == null
                        && connectedTile.right == inspectingTile.left.reversed() -> {
                    connectedTile.rightConnectedTo = inspectingTile
                    inspectingTile.leftConnectedTo = connectedTile
                }

                connectedTile.bottomConnectedTo == null
                        && inspectingTile.topConnectedTo == null
                        && connectedTile.bottom == inspectingTile.top.reversed() -> {
                    connectedTile.bottomConnectedTo = inspectingTile
                    inspectingTile.topConnectedTo = connectedTile
                }

                connectedTile.leftConnectedTo == null
                        && inspectingTile.rightConnectedTo == null
                        && connectedTile.left == inspectingTile.right.reversed() -> {
                    connectedTile.leftConnectedTo = inspectingTile
                    inspectingTile.rightConnectedTo = connectedTile
                }
            }
        }

        if (inspectingTile.topConnectedTo != null || inspectingTile.rightConnectedTo != null || inspectingTile.bottomConnectedTo != null || inspectingTile.leftConnectedTo != null) {
            connectedTiles.add(inspectingTile)
        } else {
            inspectingTile.nextFlipOrRotation()
            remainingTiles.add(inspectingTile)
        }
    }
    return connectedTiles
}

