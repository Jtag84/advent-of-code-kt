package year2023.day22

import commons.Part.Companion.part2
import java.util.*

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 7) { bricks ->
    val fallenBricks = getFallenBricks(bricks)
    fallenBricks
        .sumOf {
            val withoutIt = fallenBricks - it
            countFallenBricks(withoutIt)
        }
}

fun countFallenBricks(bricks: Set<Brick>): Int {
    val xyRangesByZ = TreeMap<Long, Set<XYRange>>()
    return bricks.sortedBy { it.third.first }
        .count { it != fallingBrick(xyRangesByZ, it) }
}