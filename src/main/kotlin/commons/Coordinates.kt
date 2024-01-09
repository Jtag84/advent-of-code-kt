package commons

import com.google.common.collect.Sets
import commons.Direction.*
import kotlin.math.abs

data class Coordinates3d (val x:Long, val y:Long, val z:Long) {
    fun getAllAroundNeighbors(): Set<Coordinates3d> {
        return Sets.cartesianProduct(setOf(x-1, x, x+1), setOf(y-1, y, y+1), setOf(z-1, z, z+1))
            .map { (x, y, z) -> Coordinates3d(x, y, z) }
            .toSet() - this
    }
}

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

enum class CardinalDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    fun move(coordinates: Coordinates, n: Long = 1): Coordinates {
        return when (this) {
            NORTH ->        coordinates.north(n)
            NORTH_EAST ->   coordinates.northEast(n)
            EAST ->         coordinates.east(n)
            SOUTH_EAST ->   coordinates.southEast(n)
            SOUTH ->        coordinates.south(n)
            SOUTH_WEST ->   coordinates.southWest(n)
            WEST ->         coordinates.west(n)
            NORTH_WEST ->   coordinates.northWest(n)
        }
    }

    fun rotateClockwise(degrees: Long) : CardinalDirection {
        return entries[((this.ordinal + degrees/45) % entries.size).toInt()]
    }

    fun rotateCounterClockwise(degrees: Long) : CardinalDirection {
        return entries[((this.ordinal + (360 - degrees)/45) % entries.size).toInt()]
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

fun Coordinates.north(n: Long = 1) = up(n)
fun Coordinates.northEast(n: Long = 1) = Coordinates(this.x + n, this.y - n)
fun Coordinates.east(n: Long = 1) = right(n)
fun Coordinates.southEast(n: Long = 1) = Coordinates(this.x + n, this.y + n)
fun Coordinates.south(n: Long = 1) = down(n)
fun Coordinates.southWest(n: Long = 1) = Coordinates(this.x - n, this.y + n)
fun Coordinates.west(n: Long = 1) = left(n)
fun Coordinates.northWest(n: Long = 1) = Coordinates(this.x - n, this.y - n)

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

fun Coordinates.findFirstInDirection(allCoordinates : Set<Coordinates>, direction: CardinalDirection, maxX: Long, maxY: Long) : Coordinates? {
    val firstCoordinates = generateSequence(this) { direction.move(it) }.drop(1).first { allCoordinates.contains(it) || it.x > maxX || it.y > maxY || it.x < 0 || it.y < 0 }
    if(firstCoordinates.x > maxX || firstCoordinates.y > maxY || firstCoordinates.x < 0 || firstCoordinates.y < 0) {
        return null
    }
    else {
        return firstCoordinates
    }
}