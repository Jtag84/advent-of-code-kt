package commons

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.math.BigInteger
import java.security.MessageDigest

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
    return this.fold(listOf(listOf())) { acc: List<List<T>>, nextElement: T ->
        val lastGroup = acc.last()
        when {
            lastGroup.isEmpty() -> acc.dropLast(1) + listOf(listOf(nextElement))
            lastGroup.last() != nextElement -> acc + listOf(listOf(nextElement))
            else -> acc.dropLast(1) + listOf(lastGroup + listOf(nextElement))
        }
    }
}

fun <T> List<T>.sublists(): List<List<T>> {
    return this.reversed().scan(emptyList()) { acc: List<T>, e ->
        acc + e
    }.reversed().map { it.reversed() }
}
