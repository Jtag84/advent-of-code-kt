package year2023.day22

import commons.Part.Companion.part1
import commons.overlap
import commons.shift
import java.util.*

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 5) { bricks ->
    val fallenBricks = getFallenBricks(bricks)
    fallenBricks
        .count {
            val withoutIt = fallenBricks - it
            willAnyBrickFall(withoutIt).not()
        }
}

fun willAnyBrickFall(bricks: Set<Brick>): Boolean {
    val xyRangesByZ = TreeMap<Long, Set<XYRange>>()
    return bricks.sortedBy { it.third.first }
        .any { it != fallingBrick(xyRangesByZ, it) }
}

typealias XYRange = Pair<XRange, YRange>

fun getFallenBricks(bricks: Set<Brick>): Set<Brick> {
    val xyRangesByZ = TreeMap<Long, Set<XYRange>>()
    return bricks.sortedBy { it.third.first }
        .fold(emptySet<Brick>()) { fallenBricks, fallingBrick ->
            fallenBricks + fallingBrick(xyRangesByZ, fallingBrick)
        }
}

fun fallingBrick(xyRangesByZ: TreeMap<Long, Set<XYRange>>, brick: Brick): Brick {
    val (xRange, yRange, zRange) = brick
    val xyRanges = xRange to yRange
    if (zRange.first == 1L) {
        xyRangesByZ.compute(zRange.last) { _, set ->
            if (set == null) {
                setOf(xyRanges)
            } else {
                (set + xyRanges)
            }
        }
        return brick
    } else {
        val minZ = 1 + findHighestZ(xyRangesByZ, zRange.first, brick)
        val newZRange = zRange.shift(minZ - zRange.first)

        xyRangesByZ.compute(newZRange.last) { _, set ->
            if (set == null) {
                setOf(xyRanges)
            } else {
                (set + xyRanges)
            }
        }

        return Brick(xRange, yRange, newZRange)
    }
}

fun findHighestZ(xyRangesByZ: TreeMap<Long, Set<XYRange>>, currentZ: Long, brick: Brick): Long {
    val (xRange, yRange, _) = brick
    return xyRangesByZ.floorEntry(currentZ)?.let { (z, xyRanges) ->
        if (xyRanges.any { it.first.overlap(xRange) && it.second.overlap(yRange) }) {
            z
        } else {
            findHighestZ(xyRangesByZ, z - 1, brick)
        }
    } ?: currentZ
}