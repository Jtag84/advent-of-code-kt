package commons

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UtilsKtTest {

    @Test
    fun groupNextToSame() {
        val groupedValues = listOf(1, 2, 2, 1, 2, 3, 3, 4, 4, 4).groupNextToSame()
        assertEquals(
            listOf(
                listOf(1),
                listOf(2, 2),
                listOf(1),
                listOf(2),
                listOf(3, 3),
                listOf(4, 4, 4),
            ), groupedValues
        )

        val groupedChars = "aaabcddde".toList().groupNextToSame().map { it.joinToString("") }
        assertEquals(
            listOf(
                "aaa",
                "b",
                "c",
                "ddd",
                "e"
            ), groupedChars
        )
    }
}