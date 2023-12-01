fun main() {
    fun part1(input: List<String>) =
        input.sumOf { line ->
            line.filter { it.isDigit() }.let { ((it.first() - '0') * 10) + (it.last() - '0') }
        }

    fun part2(input: List<String>): Int {
        val lookup = listOf(
            "zero", "0", "one", "1", "two", "2", "three", "3", "four", "4",
            "five", "5", "six", "6", "seven", "7", "eight", "8", "nine", "9"
        )
        val matchFirst = "(zero|one|two|three|four|five|six|seven|eight|nine|\\d).*".toRegex()
        val matchLast = ".*(zero|one|two|three|four|five|six|seven|eight|nine|\\d)".toRegex()

        return input.sumOf { line ->
            matchLast.find(line)!!.groupValues[1].let { lookup.indexOf(it) / 2 } +
                    matchFirst.find(line)!!.groupValues[1].let { (lookup.indexOf(it) / 2) * 10 }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
