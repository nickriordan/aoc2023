fun main() {
    data class Turn(val red: Int, val green: Int, val blue: Int)
    data class Game(val id: Int, val turns: List<Turn>)

    fun extract(col: String, s: String) = s.substringBefore(col).trim().split(' ').last().toIntOrNull() ?: 0

    fun gameData(input: List<String>) = input.map { line ->
        line.split(':').let { (gameAndId, turnsInfo) ->
            Game(id = gameAndId.drop(5).toInt(),
                turns = turnsInfo.split(';').let { s ->
                    s.map { Turn(extract("red", it), extract("green", it), extract("blue", it)) }
                })
        }
    }

    fun part1(input: List<String>) =
        gameData(input).filter { g -> g.turns.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 } }.sumOf { it.id }

    fun part2(input: List<String>) =
        gameData(input).sumOf { g -> g.turns.maxOf { it.red } * g.turns.maxOf { it.green } * g.turns.maxOf { it.blue } }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
