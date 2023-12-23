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