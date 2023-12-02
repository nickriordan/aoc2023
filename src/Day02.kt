fun main() {
    data class Turn(val red: Int, val green: Int, val blue: Int)
    data class Game(val id: Int, val turns: List<Turn>)

    fun valueOf(s: String) = s.takeIf { it.all { c -> c.isDigit() } }?.toInt() ?: 0

    fun gameData(input: List<String>)  =
        input.map { line ->
            line.split(":").let { (game, rest) ->
                val id = game.drop(5).toInt()
                val turns = rest.split(';').let { s ->
                    s.map { turn ->
                        Turn(
                            red = valueOf(turn.substringBefore("red").trim().split(' ').last()),
                            green = valueOf(turn.substringBefore("green").trim().split(' ').last()),
                            blue = valueOf(turn.substringBefore("blue").trim().split(' ').last())
                        )
                    }
                }
                Game(id, turns)
            }
        }

    fun part1(input: List<String>) =
        gameData(input).filter { g ->
            g.turns.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }
        }.sumOf { it.id }

    fun part2(input: List<String>) =
        gameData(input).sumOf { g ->
            g.turns.maxOf { it.red } * g.turns.maxOf { it.green } * g.turns.maxOf { it.blue }
        }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
