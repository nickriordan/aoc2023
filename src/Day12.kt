fun main() {
    fun part1(input: List<String>) = input.size

    fun part2(input: List<String>) = input.size

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 1)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
