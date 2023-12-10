import commons.Coordinates
import commons.aroundWithDiagonals
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
}