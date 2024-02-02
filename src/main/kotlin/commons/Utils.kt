package commons

import com.mxgraph.layout.mxCircleLayout
import com.mxgraph.layout.mxIGraphLayout
import com.mxgraph.util.mxCellRenderer
import org.apache.commons.io.FileUtils
import org.jgrapht.Graph
import org.jgrapht.ext.JGraphXAdapter
import java.awt.Color
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import javax.imageio.ImageIO
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

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

fun Iterable<Any?>.printEachLine() {
    this.forEach { it.println() }
}

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

    return this.asSequence().groupNextToSame().toList()
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

fun <T> List<T>.combinations(): List<List<T>> {
    val combinations = mutableListOf<List<T>>()
    val n = this.size

    for (i in 0 until (1 shl n)) {
        val combination = mutableListOf<T>()
        for (j in 0 until n) {
            if (i and (1 shl j) > 0) {
                combination.add(this[j])
            }
        }
        if (combination.isNotEmpty()) combinations.add(combination)
    }

    return combinations
}

@JvmName("transposeLists")
fun <T> List<List<T?>>.transpose(): List<List<T?>> {
    if (this.isEmpty() || this.first().isEmpty()) return emptyList()

    val transposeWidth = this.maxOf { it.size }
    return (0 until transposeWidth).map { col ->
        this.map { row -> row.get(col) }
    }
}

@JvmName("transposeStringLines")
fun List<String>.transpose() : List<String> {
    return this.map { it.toList() }.transpose().map { it.joinToString("") }
}

@JvmName("rotateClockwiseStringLines")
fun List<String>.rotateClockwise() : List<String> {
    return this.reversed().transpose()
}

fun <T> List<List<T?>>.rotateClockwise() : List<List<T?>> {
    return this.reversed().transpose()
}

fun kotlin.time.Duration.toColorizedString(): String {
    return when {
        this > 1.seconds -> this.toRedString()
        this > 500.milliseconds -> this.toYellowString()
        else -> this.toGreenString()
    }
}

fun Long.toColorizedReadableMemorySize(): String {
    val readableMemory = FileUtils.byteCountToDisplaySize(this)
    return when {
        this > 200_000_000 -> readableMemory.toRedString()
        this > 50_000_000 -> readableMemory.toYellowString()
        else -> readableMemory.toGreenString()
    }
}

typealias Rest = Long
typealias Modulo = Long

fun chineseRemainder(restModulos: Set< Pair<Rest, Modulo>>) : Long {
    val coprimeProduct = restModulos.map { it.second }.reduce(Math::multiplyExact)
    return restModulos.sumOf { (rest, modulo) ->
        val nProduct = coprimeProduct/modulo
        (1..modulo).asSequence().map { nProduct * it }.first { it % modulo == 1L } * rest
    } % coprimeProduct
}

fun <T> Iterable<T>.splitBy(predicate: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var sublist = mutableListOf<T>()

    for (item in this) {
        if (predicate(item)) {
            if (sublist.isNotEmpty()) {
                result.add(sublist)
                sublist = mutableListOf()
            }
        } else {
            sublist.add(item)
        }
    }

    if (sublist.isNotEmpty()) {
        result.add(sublist)
    }

    return result
}

fun <T> Sequence<T>.takeUntil(predicate: (T) -> Boolean): Sequence<T> {
    return sequence {
        for (element in this@takeUntil) {
            yield(element)
            if (predicate(element)) {
                break
            }
        }
    }
}

fun Long.isOdd() = (this % 2 == 1L)
fun Long.isEven() = (this % 2 == 0L)
fun Int.isOdd() = (this % 2 == 1)
fun Int.isEven() = (this % 2 == 0)

fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}

fun Map<Coordinates2d, EnumParser>.printMap() {
    this.entries.groupBy ({ it.key.y })
        .mapValues { it.value.sortedBy { it.key.y }.map { it.value.parsingString }.joinToString("") }
        .entries.sortedBy { it.key }.map { it.value }
        .printEachLine()
}

fun <V,E> Graph<V,E>.printGraph(imagePath : String) {
    val imgFile = File(imagePath)
    imgFile.createNewFile()
    val graphAdapter = JGraphXAdapter<V, E>(this)
    graphAdapter.edgeToCellMap.forEach { (edge, mxCell) ->
        // Assuming 'edge' has a method or property to get its weight
        mxCell.value = this.getEdgeWeight(edge)
    }

    val layout: mxIGraphLayout = mxCircleLayout(graphAdapter)
    layout.execute(graphAdapter.defaultParent)

    val image =
        mxCellRenderer.createBufferedImage(graphAdapter, null, 2.0, Color.WHITE, true, null)
    ImageIO.write(image, "PNG", imgFile)
}