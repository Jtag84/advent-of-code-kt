package commons

import kotlin.test.Test
import kotlin.test.assertEquals

class CoordinatesKtTest {

    @Test
    fun aroundWithDiagonals() {
        val aroundCoordinates = Coordinates(0,0).aroundWithDiagonals()
        val expectedCoordinates = setOf(
                Coordinates(-1, -1),
                Coordinates(-1, 0),
                Coordinates(-1, 1),
                Coordinates(0, -1),
                Coordinates(0, 1),
                Coordinates(1, -1),
                Coordinates(1, 0),
                Coordinates(1, 1),
            )
        assertEquals(expectedCoordinates, aroundCoordinates)
    }

    @Test
    fun rotateClockwise() {
        check(Coordinates(4, 1).rotateClockwise() == Coordinates(-1, 4))
        check(Coordinates(-1, 1).rotateClockwise() == Coordinates(-1, -1))
        check(Coordinates(-1, -1).rotateClockwise() == Coordinates(1, -1))
        check(Coordinates(1, -1).rotateClockwise() == Coordinates(1, 1))
        check(Coordinates(1, 1).rotateClockwise() == Coordinates(-1, 1))
        check(Coordinates(-1, 0).rotateClockwise() == Coordinates(0, -1))
        check(Coordinates(0, -1).rotateClockwise() == Coordinates(1, 0))
        check(Coordinates(1, 0).rotateClockwise() == Coordinates(0, 1))
        check(Coordinates(0, 1).rotateClockwise() == Coordinates(-1, 0))
        check(Coordinates(0, 0).rotateClockwise() == Coordinates(0, 0))
    }

    @Test
    fun rotateCounterClockwise() {
        check(Coordinates(0, 0).rotateCounterClockwise() == Coordinates(0, 0))
        check(Coordinates(0, 1).rotateCounterClockwise() == Coordinates(1, 0))
        check(Coordinates(1, 0).rotateCounterClockwise() == Coordinates(0, -1))
        check(Coordinates(0, -1).rotateCounterClockwise() == Coordinates(-1, 0))
        check(Coordinates(-1, 0).rotateCounterClockwise() == Coordinates(0, 1))
        check(Coordinates(1, 1).rotateCounterClockwise() == Coordinates(1, -1))
        check(Coordinates(1, -1).rotateCounterClockwise() == Coordinates(-1, -1))
        check(Coordinates(-1, -1).rotateCounterClockwise() == Coordinates(-1, 1))
        check(Coordinates(-1, 1).rotateCounterClockwise() == Coordinates(1, 1))
        check(Coordinates(4, 1).rotateCounterClockwise() == Coordinates(1, -4))
    }

    @Test
    fun rotateCoordinates() {
        val matrix = mapOf(
            Coordinates(0, 0) to 0,
            Coordinates(0, 1) to 1,
            Coordinates(1, 0) to 2,
            Coordinates(1, 1) to 3,
        )
        // 0 2
        // 1 3

        val rotatedMatrix = matrix.toList().associate { it.first.rotateCounterClockwise().down(1) to it.second }
        // 2 3
        // 0 1
        val expectedMatrix = mapOf(
            Coordinates(0, 0) to 2,
            Coordinates(0, 1) to 0,
            Coordinates(1, 0) to 3,
            Coordinates(1, 1) to 1,
        )

        check(expectedMatrix == rotatedMatrix)
    }
}