fun main() {
    fun part1(input: List<String>) = input.size

    fun part2(input: List<String>) = input.size

    val testInput = readInput("Day04_test")
    check(part2(testInput) == 1)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
