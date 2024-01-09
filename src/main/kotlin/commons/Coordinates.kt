package commons

import com.google.common.collect.Sets
import commons.Direction.*
import kotlin.math.abs

interface Coordinates<out T : Coordinates<T>> {
    fun allAround() : Set<T>
}

data class Coordinates4d (val x:Long, val y:Long, val z:Long, val w: Long) : Coordinates<Coordinates4d> {
    override fun allAround(): Set<Coordinates4d> {
        return Sets.cartesianProduct(setOf(x-1, x, x+1), setOf(y-1, y, y+1), setOf(z-1, z, z+1), setOf(w-1, w, w+1))
            .map { (x, y, z, w) -> Coordinates4d(x, y, z, w) }
            .toSet() - this
    }

}
data class Coordinates3d (val x:Long, val y:Long, val z:Long) : Coordinates<Coordinates3d> {
    override fun allAround(): Set<Coordinates3d> {
        return Sets.cartesianProduct(setOf(x-1, x, x+1), setOf(y-1, y, y+1), setOf(z-1, z, z+1))
            .map { (x, y, z) -> Coordinates3d(x, y, z) }
            .toSet() - this
    }
}

data class Coordinates2d (val x:Long, val y:Long) : Comparable<Coordinates2d>, Coordinates<Coordinates2d> {
    override fun compareTo(other: Coordinates2d): Int {
        return if(x == other.x) {
            y.compareTo(other.y)
        }
        else {
            x.compareTo(other.x)
        }
    }

    override fun allAround(): Set<Coordinates2d> {
        return setOf(
            Coordinates2d(x - 1, y - 1),
            Coordinates2d(x - 1, y),
            Coordinates2d(x - 1, y + 1),
            Coordinates2d(x, y - 1),
            Coordinates2d(x, y + 1),
            Coordinates2d(x + 1, y - 1),
            Coordinates2d(x + 1, y),
            Coordinates2d(x + 1, y + 1),
        )
    }
}

fun Coordinates3d.xRotate90CounterClockwise() = Coordinates3d(this.x, -this.z, this.y)
fun Coordinates3d.yRotate90CounterClockwise() = Coordinates3d(this.z, this.y, -this.x)
fun Coordinates3d.zRotate90CounterClockwise() = Coordinates3d(-this.y, this.x, this.z)
fun Coordinates3d.xRotate90Clockwise() = Coordinates3d(this.x, this.z, -this.y)
fun Coordinates3d.yRotate90Clockwise() = Coordinates3d(-this.z, this.y, this.x)
fun Coordinates3d.zRotate90Clockwise() = Coordinates3d(this.y, -this.x, this.z)


enum class CardinalDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    fun move(coordinates: Coordinates2d, n: Long = 1): Coordinates2d {
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

    fun move(coordinates: Coordinates2d, n: Long = 1): Coordinates2d {
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

fun Coordinates2d.cardinals(): Collection<Coordinates2d> {
    return listOf(north(), east(), south(), west())
}

fun Coordinates2d.directionTo(coordinates: Coordinates2d): Direction? {
    return when (coordinates) {
        this.right() -> RIGHT
        this.left() -> LEFT
        this.up() -> UP
        this.down() -> DOWN
        else -> null
    }
}

fun Coordinates2d.manhattanDistance(coordinates: Coordinates2d): Long {
    return abs(this.x - coordinates.x) + abs(this.y - coordinates.y)
}

fun Coordinates3d.manhattanDistance(coordinates: Coordinates3d): Long {
    return abs(this.x - coordinates.x) + abs(this.y - coordinates.y) + abs(this.z - coordinates.z)
}

fun Coordinates2d.up(n: Long = 1): Coordinates2d {
    return Coordinates2d(x, y - n)
}

fun Coordinates2d.down(n: Long = 1): Coordinates2d {
    return Coordinates2d(x, y + n)
}

fun Coordinates2d.left(n: Long = 1): Coordinates2d {
    return Coordinates2d(x - n, y)
}

fun Coordinates2d.right(n: Long = 1): Coordinates2d {
    return Coordinates2d(x + n, y)
}

fun Coordinates2d.north(n: Long = 1) = up(n)
fun Coordinates2d.northEast(n: Long = 1) = Coordinates2d(this.x + n, this.y - n)
fun Coordinates2d.east(n: Long = 1) = right(n)
fun Coordinates2d.southEast(n: Long = 1) = Coordinates2d(this.x + n, this.y + n)
fun Coordinates2d.south(n: Long = 1) = down(n)
fun Coordinates2d.southWest(n: Long = 1) = Coordinates2d(this.x - n, this.y + n)
fun Coordinates2d.west(n: Long = 1) = left(n)
fun Coordinates2d.northWest(n: Long = 1) = Coordinates2d(this.x - n, this.y - n)

fun Coordinates2d.rotateCounterClockwise() = Coordinates2d(y, -x)
fun Coordinates2d.rotateClockwise() = Coordinates2d(-y, x)

fun Coordinates2d.flip(): Coordinates2d {
    return Coordinates2d(y, x)
}

fun Coordinates2d.negate(): Coordinates2d {
    return Coordinates2d(-x, -y)
}

fun Map<Coordinates2d, EnumParser>.toStringMap(): String {
    return this.entries.groupBy ({ it.key.y }, {it.key.x to it.value.parsingString} )
        .mapValues { it.value.sortedBy { it.first }.map { it.second }.joinToString("") }
        .entries
        .sortedBy { it.key }
        .map { it.value }
        .joinToString("\n")
}

fun Coordinates2d.findFirstInDirection(allCoordinates : Set<Coordinates2d>, direction: CardinalDirection, maxX: Long, maxY: Long) : Coordinates2d? {
    val firstCoordinates = generateSequence(this) { direction.move(it) }.drop(1).first { allCoordinates.contains(it) || it.x > maxX || it.y > maxY || it.x < 0 || it.y < 0 }
    if(firstCoordinates.x > maxX || firstCoordinates.y > maxY || firstCoordinates.x < 0 || firstCoordinates.y < 0) {
        return null
    }
    else {
        return firstCoordinates
    }
}