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

    @Test
    fun chineseRemainder() {
        val chineseRemainder1 = chineseRemainder(
            setOf(
                2L to 3,
                3L to 5,
                2L to 7
            )
        )
        check (chineseRemainder1 == 23L)

        val chineseRemainder2 = chineseRemainder(
            setOf(
                0L to 17,
                11L to 13,
                16L to 19
            )
        )
        check (chineseRemainder2 == 3417L)

        val chineseRemainder3 = chineseRemainder(
            setOf(
                0L to 7,
                12L to 13,
                55L to 59,
                25L to 31,
                12L to 19,
            )
        )
        check (chineseRemainder3 == 1_068_781L)
    }
}