import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDate
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: LocalDate) = Path("${getPathPrefixString(day)}.txt").readLines()
fun readTestInput(day: LocalDate) = Path("${getPathPrefixString(day)}_test.txt").readLines()

private fun getPathPrefixString(day: LocalDate) = "src/year${day.year}/Day${String.format("%02d", day.dayOfMonth)}"

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
