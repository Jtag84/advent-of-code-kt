package commons

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.math.BigInteger
import java.security.MessageDigest

const val RED_FOREGROUND = "\u001b[31m"
const val YELLOW_FOREGROUND = "\u001b[33m"
const val GREEN_FOREGROUND = "\u001b[32m"
const val RESET_COLORS = "\u001b[0m"

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun Any?.toRedString() = "$RED_FOREGROUND$this$RESET_COLORS"
fun Any?.toYellowString() = "$YELLOW_FOREGROUND$this$RESET_COLORS"
fun Any?.toGreenString() = "$GREEN_FOREGROUND$this$RESET_COLORS"

fun <K> Map<K, Int>.mergeAndSumValues(otherMap: Map<K, Int>): Map<K, Int> {
    return this.merge(otherMap, Int::plus)
}

fun <K, V> Map<K, V>.merge(otherMap: Map<K, V>, mergeValueFunction: (V, V) -> V): Map<K, V> {
    return (this.toList() + otherMap.toList())
        .groupBy({ it.first }, { it.second })
        .mapValues { it.value.reduce(mergeValueFunction) }
}

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}

fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

inline fun <T, R> Sequence<T>.foldUntil(initial: R, condition: (R) -> Boolean, operation: (acc: R, T) -> R): R {
    var accumulator = initial
    for (element in this) {
        accumulator = operation(accumulator, element)
        if (condition(accumulator)) break
    }
    return accumulator
}

fun <T> List<T>.groupNextToSame(): List<List<T>> {
    if (this.isEmpty()) return listOf()

    val result = mutableListOf<List<T>>()
    var currentGroup = mutableListOf(this.first())

    for (i in 1 until this.size) {
        if (this[i] == this[i - 1]) {
            currentGroup.add(this[i])
        } else {
            result.add(currentGroup)
            currentGroup = mutableListOf(this[i])
        }
    }
    result.add(currentGroup)

    return result
}

fun <T> Sequence<T>.groupNextToSame(): Sequence<List<T>> {
    return sequence {
        val iterator = this@groupNextToSame.iterator()

        if (!iterator.hasNext()) return@sequence

        var currentElement = iterator.next()
        var currentGroup = mutableListOf(currentElement)

        while (iterator.hasNext()) {
            val nextElement = iterator.next()
            if (nextElement == currentElement) {
                currentGroup.add(nextElement)
            } else {
                yield(currentGroup)
                currentElement = nextElement
                currentGroup = mutableListOf(currentElement)
            }
        }

        yield(currentGroup)
    }
}

fun <T> List<T>.sublists(): List<List<T>> {
    return this.reversed().scan(emptyList()) { acc: List<T>, e ->
        acc + e
    }.reversed().map { it.reversed() }
}


fun <T> List<List<T?>>.transpose(): List<List<T?>> {
    if (this.isEmpty() || this.first().isEmpty()) return emptyList()

    val transposeWidth = this.maxOf { it.size }
    return (0 until transposeWidth).map { col ->
        this.map { row -> row.getOrNull(col) }
    }
}
