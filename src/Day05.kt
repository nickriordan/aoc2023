fun main() {

    fun makeLookup(input: List<String>, sectionName: String): (Long) -> Long {
        data class Lookup(val srcRange: LongRange, val offset: Long)

        val lookup =
            input.asSequence().dropWhile { line ->
                line != "$sectionName map:"
            }.drop(1).takeWhile { line ->
                line.isNotEmpty()
            }.map { line ->
                line.parseLongs().let { (destStart, srcStart, length) ->
                    Lookup(srcStart..<srcStart + length, destStart - srcStart)
                }
            }.sortedBy { it.srcRange.first }.toList()

        return { v: Long -> lookup.find { v in it.srcRange }?.run { v + offset } ?: v }
    }

    fun makeLookups(vararg lookups: (Long) -> Long) = { v: Long -> lookups.fold(v) { r, f -> f(r) } }

    fun inputToLookup(input: List<String>) = makeLookups(
        makeLookup(input, "seed-to-soil"),
        makeLookup(input, "soil-to-fertilizer"),
        makeLookup(input, "fertilizer-to-water"),
        makeLookup(input, "water-to-light"),
        makeLookup(input, "light-to-temperature"),
        makeLookup(input, "temperature-to-humidity"),
        makeLookup(input, "humidity-to-location")
    )

    fun part1(input: List<String>): Long {
        val lookup = inputToLookup(input)
        val seeds = input.first().drop(7).parseLongs()
        return seeds.minOf { seed -> lookup(seed) }
    }

    fun part2(input: List<String>): Long {
        val lookup = inputToLookup(input)
        val seeds = input.first().drop(7).parseLongs().windowed(2, 2) { (start, len) ->
            LongRange(start, start + len)
        }.asSequence().flatten()
        return seeds.minOf { seed -> lookup(seed) }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
