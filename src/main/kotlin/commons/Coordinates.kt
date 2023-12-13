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

fun Coordinates.up(): Coordinates {
    return Coordinates(x, y - 1)
}

fun Coordinates.down(): Coordinates {
    return Coordinates(x, y+1)
}
fun Coordinates.left(): Coordinates {
    return Coordinates(x-1, y)
}
fun Coordinates.right(): Coordinates {
    return Coordinates(x + 1, y)
}

fun Coordinates.north() = up()
fun Coordinates.south() = down()
fun Coordinates.east() = right()
fun Coordinates.west() = left()
