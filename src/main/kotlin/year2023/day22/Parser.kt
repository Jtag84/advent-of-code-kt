package year2023.day22

import cc.ekblad.konbini.*
import kotlin.math.max
import kotlin.math.min

val inputParser: Parser<Set<Brick>> = parser {
    chain1(brickParser, whitespace).terms.toSet()
}

typealias XRange = LongRange
typealias YRange = LongRange
typealias ZRange = LongRange

typealias Brick = Triple<XRange, YRange, ZRange>

data class BrickNode(
    val xRange: XRange,
    val yRange: YRange,
    val zRange: ZRange,
    val supportingBricks: MutableSet<BrickNode>,
    val supportedBy: MutableSet<BrickNode>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BrickNode

        if (xRange != other.xRange) return false
        if (yRange != other.yRange) return false
        if (zRange != other.zRange) return false

        return true
    }

    override fun hashCode(): Int {
        var result = xRange.hashCode()
        result = 31 * result + yRange.hashCode()
        result = 31 * result + zRange.hashCode()
        return result
    }

    override fun toString(): String {
        return "BrickNode(xRange=$xRange, yRange=$yRange, zRange=$zRange)"
    }
}

val brickParser: Parser<Brick> = parser {
    val (xLeft, yLeft, zLeft) = parse3dCoordinates()
    char('~')
    val (xRight, yRight, zRight) = parse3dCoordinates()

    val xRange = min(xLeft, xRight)..max(xLeft, xRight)
    val yRange = min(yLeft, yRight)..max(yLeft, yRight)
    val zRange = min(zLeft, zRight)..max(zLeft, zRight)
    Brick(xRange, yRange, zRange)
}

val parse3dCoordinates = parser {
    val x = integer()
    char(',')
    val y = integer()
    char(',')
    val z = integer()
    Triple(x, y, z)
}