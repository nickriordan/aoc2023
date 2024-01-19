fun main() {

    fun List<Int>.allZero() = this.all { it == 0 }

    fun part1(input: List<String>): Int {
        fun processData(data: List<Int>): Int =
            if (data.allZero()) 0 else
                processData(data.zipWithNext().map { it.second - it.first }).let { r -> (data.last() + r) }

        return input.map { it.parseInts() }.sumOf { processData(it) }
    }

    fun part2(input: List<String>): Int {
        fun processData(data: List<Int>): Int =
            if (data.allZero()) 0 else
                processData(data.zipWithNext().map { it.second - it.first }).let { r -> data.first() - r }

        return input.map { it.parseInts() }.sumOf { processData(it) }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
