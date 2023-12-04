import kotlin.math.min

fun main() {
    fun getScores(lines: List<String>) = lines.associate { line ->
        line.drop(5).split(':').let { (id, rest) ->
            rest.split('|').map { it.trim().split("\\s+".toRegex()).map { n -> n.toInt() } }.let { r ->
                id.trim().toInt() to r.first().toSet().intersect(r.last().toSet()).count()
            }
        }
    }  // Map of Card id to count of correct for each card

    fun part1(scores: Map<Int, Int>) = scores.values.sumOf { listOf(0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512)[it] }

    fun part2(scores: Map<Int, Int>): Int {
        fun collect(id: Int): Int = (id + 1..min(id + scores[id]!!, scores.size)).sumOf { collect((it)) } + 1
        return scores.keys.sumOf { collect(it) }
    }

    val testInput = getScores(readInput("Day04_test"))
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = getScores(readInput("Day04"))
    part1(input).println()
    part2(input).println()
}
