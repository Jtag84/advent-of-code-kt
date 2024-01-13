package year2020.day20

import com.google.common.collect.Sets
import commons.Part.Companion.part2
import commons.*

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val seaMonster =
    """
    |                  # 
    |#    ##    ##    ###
    | #  #  #  #  #  #   
    """.trimMargin().lines()

val part2 = part2(inputParser, 273) { tiles ->
    val connectedTiles = getConnectedTiles(tiles)

    val topLeftCorner = connectedTiles.first { it.leftConnectedTo == null && it.topConnectedTo == null && it.rightConnectedTo != null && it.bottomConnectedTo != null }

    val reconstitutedImage = reconstituteImage(topLeftCorner)

    val imageMaxX = reconstitutedImage[0].length - 1
    val imageMaxY = reconstitutedImage.size - 1

    val possibleSeaMonsterParts = reconstitutedImage.getPoundSignCoordinates()

    val allSeaMonsterOrientationCoordinates = generateSequence(listOf(seaMonster, seaMonster.reversed())) {seaMonsters -> seaMonsters.map { it.rotateClockwise() }}.take(4).flatten().map { it.getPoundSignCoordinates() }

    val allSeaMonsterParts = allSeaMonsterOrientationCoordinates.map { seaMonsterOrientation ->
        val maxX = seaMonsterOrientation.maxOf { it.x }
        val maxY = seaMonsterOrientation.maxOf { it.y }

        val xOffsets = (0..(imageMaxX - maxX)).toSet()
        val yOffsets = (0..(imageMaxY - maxY)).toSet()

        val allXYOffsets = Sets.cartesianProduct(xOffsets, yOffsets)
        allXYOffsets.asSequence().map { xyOffset ->
            seaMonsterOrientation.map { (x,y) -> Coordinates2d(x + xyOffset[0], y + xyOffset[1]) }.toSet()
        }
        .filter { possibleSeaMonsterParts.containsAll(it) }
        .flatten()
        .toSet()
    }.flatten().toSet()

    possibleSeaMonsterParts.count() - allSeaMonsterParts.count()
}

fun reconstituteImage(topLeftCorner : Tile) : List<String> {
    val allTiles : MutableList<MutableList<Tile>> = mutableListOf()
    var nextTileUnder: Tile? = topLeftCorner
    var nextTileOnTheRight: Tile? = topLeftCorner
    while(nextTileUnder != null) {
        val row = mutableListOf<Tile>()
        while (nextTileOnTheRight != null) {
            row.add(nextTileOnTheRight)
            nextTileOnTheRight = nextTileOnTheRight.rightConnectedTo
        }
        allTiles.add(row)
        nextTileUnder = nextTileUnder.bottomConnectedTo
        nextTileOnTheRight = nextTileUnder
    }
    return allTiles.map { row ->
        row.map { tile ->
            tile.getFullImageWithoutBorders()
        }.reduce { acc, tileLines ->
            acc.zip(tileLines) { leftLine, rightLine -> leftLine + rightLine }
        }
    }
    .flatten()
}

private fun List<String>.getPoundSignCoordinates() : Set<Coordinates2d> {
    return this.mapIndexedNotNull { y, line ->
        line.mapIndexedNotNull { x, c -> if(c == '#') {Coordinates2d(x.toLong(), y.toLong())} else {null} }
    }.flatten().toSet()
}