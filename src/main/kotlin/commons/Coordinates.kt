package commons

import kotlin.math.abs

data class Coordinates (val x:Long, val y:Long)

fun Coordinates.manhattanDistance(coordinates: Coordinates): Long {
    return abs(this.x - coordinates.x) + abs(this.y - coordinates.y)
}

fun Coordinates.aroundWithDiagonals(): Set<Coordinates> {
    return setOf(
        Coordinates(x - 1, y - 1),
        Coordinates(x - 1, y),
        Coordinates(x - 1, y + 1),
        Coordinates(x, y - 1),
        Coordinates(x, y + 1),
        Coordinates(x + 1, y - 1),
        Coordinates(x + 1, y),
        Coordinates(x + 1, y + 1),
    )
}

fun Coordinates.up() = up(1)
fun Coordinates.up(n: Long): Coordinates {
    return Coordinates(x, y - n)
}

fun Coordinates.down() = down(1)

fun Coordinates.down(n: Long): Coordinates {
    return Coordinates(x, y + n)
}
fun Coordinates.left(): Coordinates {
    return Coordinates(x-1, y)
}
fun Coordinates.right() = right(1)
fun Coordinates.right(n: Long): Coordinates {
    return Coordinates(x + n, y)
}

fun Coordinates.north() = up()
fun Coordinates.south() = down()
fun Coordinates.east() = right()
fun Coordinates.west() = left()

fun Coordinates.rotateCounterClockwise() = Coordinates(y, -x)
fun Coordinates.rotateClockwise() = Coordinates(-y, x)

fun Coordinates.flip(): Coordinates {
    return Coordinates(y, x)
}

fun Coordinates.negate(): Coordinates {
    return Coordinates(-x, -y)
}

