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
    // less efficient solution
//    val fallenBricks = getFallenBricks(bricks)
//    fallenBricks
//        .count {
//            val withoutIt = fallenBricks - it
//            willAnyBrickFall(withoutIt).not()
//        }

    getFallenBricksWithSupportingData(bricks)
        .count { fallenBrick ->
            fallenBrick.supportingBricks.isEmpty()
                    || fallenBrick.supportingBricks.all { it.supportedBy.count() > 1 }
        }
}

fun getFallenBricksWithSupportingData(bricks: Set<Brick>): Set<BrickNode> {
    val xyRangesByZ = TreeMap<Long, Set<XYRange>>()
    return bricks.sortedBy { it.third.first }
        .fold(emptySet()) { fallenBricks, fallingBrick ->
            val (fallenXRange, fallenYRange, fallenZRange) = fallingBrick(xyRangesByZ, fallingBrick)
            val fallenBrick = BrickNode(fallenXRange, fallenYRange, fallenZRange, mutableSetOf(), mutableSetOf())
            fallenBricks
                .filter { it.zRange.last == (fallenZRange.first - 1) }
                .filter { it.xRange.overlap(fallenXRange) && it.yRange.overlap(fallenYRange) }
                .forEach {
                    it.supportingBricks.add(fallenBrick)
                    fallenBrick.supportedBy.add(it)
                }

            fallenBricks + fallenBrick
        }
}

fun willAnyBrickFall(bricks: Set<Brick>): Boolean {
    val xyRangesByZ = TreeMap<Long, Set<XYRange>>()
    return bricks.sortedBy { it.third.first }
        .any { it != fallingBrick(xyRangesByZ, it) }
}

typealias XYRange = Pair<XRange, YRange>

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