fun main() {
    data class Turn(val r: Int, val g: Int, val b: Int)
    data class Game(val id: Int, val turns: List<Turn>)

    fun String.cubes(colour: String) = substringBefore(colour).trim().split(' ').last().toIntOrNull() ?: 0

    fun gameData(input: List<String>) = input.map { line ->
        line.drop(5).split(':').let { (gameId, turns) ->
            Game(gameId.toInt(), turns.split(';').run {
                map { Turn(it.cubes("red"), it.cubes("green"), it.cubes("blue")) }
            })
        }
    }

    fun part1(games: List<Game>) =
        games.filter { game -> game.turns.all { it.r <= 12 && it.g <= 13 && it.b <= 14 } }.sumOf { it.id }

    fun part2(games: List<Game>) =
        games.sumOf { game -> game.turns.run { maxOf { it.r } * maxOf { it.g } * maxOf { it.b } } }

    val testGames = gameData(readInput("Day02_test"))
    check(part1(testGames) == 8)
    check(part2(testGames) == 2286)

    val games = gameData(readInput("Day02"))
    part1(games).println()
    part2(games).println()
}
