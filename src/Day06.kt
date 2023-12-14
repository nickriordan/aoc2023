fun main() {
    data class Race(val time: Long, val previousWinningDistance: Long) {
        fun myDistance(buttonTime: Long) = (time - buttonTime) * buttonTime
        fun countWinWays() = (1..<time).count { buttonTime -> myDistance(buttonTime) > previousWinningDistance }
    }

    fun part1(input: List<String>) =
        input[0].drop(10).trim().parseLongs().zip(input[1].drop(10).trim().parseLongs()).map { (t, d) ->
            Race(t, d)
        }.fold(1) { res, race -> res * race.countWinWays() }

    fun part2(input: List<String>) =
        Race(input[0].filter { it.isDigit() }.toLong(), input[1].filter { it.isDigit() }.toLong()).countWinWays()

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
