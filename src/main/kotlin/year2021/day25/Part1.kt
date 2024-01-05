package year2021.day25

import commons.Coordinates
import commons.Part.Companion.part1
import commons.east
import commons.south

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 58) { seaMap ->
    val maxX = seaMap.keys.maxOf { it.x }
    val maxY = seaMap.keys.maxOf { it.y }
    val coordinatesBySeaCucumber = seaMap.entries.groupBy ({ it.value }, {it.key})
    val southSeaCucumbers = coordinatesBySeaCucumber[SeaCucumber.SOUTH]!!.toSet()
    val eastSeaCucumbers = coordinatesBySeaCucumber[SeaCucumber.EAST]!!.toSet()

    generateSequence (eastSeaCucumbers to southSeaCucumbers) { (currentEastCucumbers, currentSouthCucmbers) ->
            val (newEast, newSouth) = movesOneStep(currentEastCucumbers, currentSouthCucmbers, maxX, maxY)

            if(newEast == currentEastCucumbers && newSouth == currentSouthCucmbers) {
                null
            }
            else {
                newEast to newSouth
            }
        }
        .count()
}

fun movesOneStep(eastSeaCucumbers : Set<Coordinates>, southSeaCucumbers : Set<Coordinates>, maxX: Long, maxY:Long) : Pair<Set<Coordinates>, Set<Coordinates>> {
    val newEastSeaCucumbers = eastSeaCucumbers.map {
            val eastCoordinates = it.east()
            val newCoordinates = if (eastCoordinates.x > maxX) {
                Coordinates(0, eastCoordinates.y)
            }
            else {
                eastCoordinates
            }

            if(southSeaCucumbers.contains(newCoordinates) || eastSeaCucumbers.contains(newCoordinates) ) {
                it
            }
            else {
                newCoordinates
            }
        }
        .toSet()

    val newSouthSeaCucumber = southSeaCucumbers.map {
            val southCoordinates = it.south()
            val newCoordinates = if (southCoordinates.y > maxY) {
                Coordinates(southCoordinates.x, 0)
            }
            else {
                southCoordinates
            }

            if(southSeaCucumbers.contains(newCoordinates) || newEastSeaCucumbers.contains(newCoordinates) ) {
                it
            }
            else {
                newCoordinates
            }
        }
        .toSet()

    return newEastSeaCucumbers to newSouthSeaCucumber
}