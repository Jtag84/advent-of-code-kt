package commons

import commons.Direction.*
import kotlin.math.abs

data class Coordinates3d (val x:Long, val y:Long, val z:Long)

fun Coordinates3d.xRotate90CounterClockwise() = Coordinates3d(this.x, -this.z, this.y)
fun Coordinates3d.yRotate90CounterClockwise() = Coordinates3d(this.z, this.y, -this.x)
fun Coordinates3d.zRotate90CounterClockwise() = Coordinates3d(-this.y, this.x, this.z)
fun Coordinates3d.xRotate90Clockwise() = Coordinates3d(this.x, this.z, -this.y)
fun Coordinates3d.yRotate90Clockwise() = Coordinates3d(-this.z, this.y, this.x)
fun Coordinates3d.zRotate90Clockwise() = Coordinates3d(this.y, -this.x, this.z)

data class Coordinates (val x:Long, val y:Long) : Comparable<Coordinates> {
    override fun compareTo(other: Coordinates): Int {
        return if(x == other.x) {
            y.compareTo(other.y)
        }
        else {
            x.compareTo(other.x)
        }
    }
}

enum class Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT;

    fun move(coordinates: Coordinates, n: Long = 1): Coordinates {
        return when (this) {
            UP -> coordinates.up(n)
            DOWN -> coordinates.down(n)
            RIGHT -> coordinates.right(n)
            LEFT -> coordinates.left(n)
        }
    }

    fun opposite(): Direction {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            RIGHT -> LEFT
            LEFT -> RIGHT
        }
    }
}

fun Coordinates.cardinals(): Collection<Coordinates> {
    return listOf(north(), east(), south(), west())
}

fun Coordinates.directionTo(coordinates: Coordinates): Direction? {
    return when (coordinates) {
        this.right() -> RIGHT
        this.left() -> LEFT
        this.up() -> UP
        this.down() -> DOWN
        else -> null
    }
}

fun Coordinates.manhattanDistance(coordinates: Coordinates): Long {
    return abs(this.x - coordinates.x) + abs(this.y - coordinates.y)
}

fun Coordinates3d.manhattanDistance(coordinates: Coordinates3d): Long {
    return abs(this.x - coordinates.x) + abs(this.y - coordinates.y) + abs(this.z - coordinates.z)
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

fun Coordinates.up(n: Long = 1): Coordinates {
    return Coordinates(x, y - n)
}

fun Coordinates.down(n: Long = 1): Coordinates {
    return Coordinates(x, y + n)
}

fun Coordinates.left(n: Long = 1): Coordinates {
    return Coordinates(x - n, y)
}

fun Coordinates.right(n: Long = 1): Coordinates {
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

fun Map<Coordinates, EnumParser>.toStringMap(): String {
    return this.entries.groupBy ({ it.key.y }, {it.key.x to it.value.parsingString} )
        .mapValues { it.value.sortedBy { it.first }.map { it.second }.joinToString("") }
        .entries
        .sortedBy { it.key }
        .map { it.value }
        .joinToString("\n")
}