import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.min
import kotlin.math.max

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

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

/**
 * extract a list of ints from this string
 */
fun String.parseInts() = this.trim().split("\\s+".toRegex()).map { it.toInt() }

fun String.parseLongs() = this.trim().split("\\s+".toRegex()).map { it.toLong() }

fun LongRange.intersection(other: LongRange): LongRange {
    val first = max(this.first, other.first)
    val last = min(this.last, other.last)
    return if (first <= last) LongRange(first, last) else LongRange.EMPTY
}

fun main() {
    println((1L..7L).intersection(5L..10L))
    println((5L..10L).intersection(1L..7L))
    println((1L..5L).intersection(7L..10L))
    println((7L..10L).intersection(1L..6L))
    println((47L..103L).intersection(98L..99L))
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}