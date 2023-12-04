import kotlin.math.min

fun main() {
    fun readCards(lines: List<String>): Map<Int, Int> = lines.associate { line ->
        line.drop(5).split(':').let { (id, rest) ->
            rest.split('|').map { it.trim().split("\\s+".toRegex()).map { n -> n.toInt() } }.let { r ->
                id.trim().toInt() to r.first().toSet().intersect(r.last().toSet()).count()
            }
        }
    }  // Map of Card id to count of correct for each card

    fun part1(input: List<String>) =
        readCards(input).toList().sumOf { listOf(0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512)[it.second] }

    fun part2(input: List<String>): Int {
        val scores = readCards(input)
        fun cardsWon(id: Int) = if (scores[id]!! == 0) null else id + 1..min(id + scores[id]!!, scores.size)
        fun collect(id: Int): Int = 1 + (cardsWon(id)?.sumOf { collect(it) } ?: 0)
        return scores.keys.sumOf { collect(it) }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
