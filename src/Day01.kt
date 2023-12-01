fun main() {
    fun part1(input: List<String>) =
        input.sumOf { line ->
            line.filter { it.isDigit() }.let { ((it.first() - '0') * 10) + (it.last() - '0') }
        }

    fun part2(input: List<String>): Int {
        val lookup = mapOf(
            "zero" to 0,
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
            "0" to 0,
            "1" to 1,
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9
        )

        val reversedLookup = lookup.map { it.key.reversed() to it.value }.toMap()

        return input.sumOf { line ->
            (lookup[line.findAnyOf(lookup.keys)!!.second]!! * 10) +
                    reversedLookup[line.reversed().findAnyOf(reversedLookup.keys)!!.second]!!
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
