package year2019.day20

import cc.ekblad.konbini.*
import commons.*

typealias MazeMap = Map<Coordinates2d, MazeElement>

val inputParser: Parser<MazeMap> = parser {
    val mazeMap = coordinatesParser(mazeElementParser)().toMap()
    val finalMazeMap = buildPortals(mazeMap)
    finalMazeMap
}

private fun buildPortals(mazeMap: Map<Coordinates2d, MazeElement>): Map<Coordinates2d, MazeElement> {
    val partialPortals = mazeMap.entries.filter { it.value is MazeElement.PartialPortal }.map { it.key }

    val verticalPortals = partialPortals.groupBy { it.x }
        .mapValues {
            it.value
                .sortedBy { it.y }
                .zipWithNext { left, right ->
                    val (_, leftY) = left
                    val (_, rightY) = right
                    val leftName = (mazeMap[left]!! as MazeElement.PartialPortal).name
                    val rightName = (mazeMap[right]!! as MazeElement.PartialPortal).name
                    if ((rightY - leftY) != 1L) {
                        return@zipWithNext null
                    }

                    when {
                        mazeMap[left.north()]?.equals(MazeElement.Empy) ?: false -> {
                            leftName.toString() + rightName to (left to left.north())
                        }

                        mazeMap[right.south()]?.equals(MazeElement.Empy) ?: false -> {
                            leftName.toString() + rightName to (right to right.south())
                        }

                        else -> null
                    }
                }
                .filterNotNull()
        }
        .values
        .flatten()

    val horizontalPortals = partialPortals.groupBy { it.y }
        .mapValues {
            it.value
                .sortedBy { it.x }
                .zipWithNext { left, right ->
                    val (leftX, _) = left
                    val (rightX, _) = right
                    val leftName = (mazeMap[left]!! as MazeElement.PartialPortal).name
                    val rightName = (mazeMap[right]!! as MazeElement.PartialPortal).name
                    if ((rightX - leftX) != 1L) {
                        return@zipWithNext null
                    }

                    when {
                        mazeMap[left.west()]?.equals(MazeElement.Empy) ?: false -> {
                            leftName.toString() + rightName to (left to left.west())
                        }

                        mazeMap[right.east()]?.equals(MazeElement.Empy) ?: false -> {
                            leftName.toString() + rightName to (right to right.east())
                        }

                        else -> null
                    }
                }
                .filterNotNull()
        }
        .values
        .flatten()

    val maxX = mazeMap.maxOf { it.key.x }
    val maxY = mazeMap.maxOf { it.key.y }
    val finalMazeMap = mazeMap.filterValues { it !is MazeElement.PartialPortal }.toMutableMap()
    val portalToCoordinates = (horizontalPortals + verticalPortals).groupBy({ it.first }, { it.second })
    portalToCoordinates.filterValues { it.size > 1 }.forEach { (portalName, atToCoordinates) ->
        check(atToCoordinates.size == 2)
        var (at1, goingTo1) = atToCoordinates.first()
        var (at2, goingTo2) = atToCoordinates.last()

        if(at1.x == 1L || (maxX - at1.x) == 1L || at1.y == 1L || (maxY - at1.y) == 1L) {
            finalMazeMap[at1] = MazeElement.OuterPortal(portalName, goingTo2)
            finalMazeMap[at2] = MazeElement.InnerPortal(portalName, goingTo1)
        } else {
            finalMazeMap[at1] = MazeElement.InnerPortal(portalName, goingTo2)
            finalMazeMap[at2] = MazeElement.OuterPortal(portalName, goingTo1)
        }
    }

    val (portalAACoordinate, start) = portalToCoordinates["AA"]!!.first()
    val (portalZZCoordinate, finish) = portalToCoordinates["ZZ"]!!.first()
    finalMazeMap[start] = MazeElement.Start
    finalMazeMap[portalAACoordinate] = MazeElement.Wall
    finalMazeMap[finish] = MazeElement.Finish
    finalMazeMap[portalZZCoordinate] = MazeElement.Wall
    return finalMazeMap
}

sealed class MazeElement {
    data object Wall: MazeElement()
    data object Empy: MazeElement()
    data object Outside:MazeElement()
    data object Start: MazeElement()
    data object Finish: MazeElement()

    data class InnerPortal(val name: String, val toCoordinates: Coordinates2d) : MazeElement()
    data class OuterPortal(val name: String, val toCoordinates: Coordinates2d) : MazeElement()
    data class PartialPortal(val name: Char) : MazeElement()
}

val mazeElementParser = parser { oneOf(wallParser, emptyParser, outsideParser, partialPortalParser) }
val wallParser = parser { char('#') }.map { MazeElement.Wall }
val emptyParser = parser { char('.') }.map { MazeElement.Empy }
val outsideParser = parser { char(' ') }.map { MazeElement.Outside }
val partialPortalParser = parser { regex("[A-Z]") }.map { MazeElement.PartialPortal(it.first()) }