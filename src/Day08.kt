fun main() {
    data class Node(val left: String, val right: String)

    fun readNetwork(input: List<String>) =
        input.drop(2).associate { line ->
            line.filterNot { it.isWhitespace() || it in "=()," }.let { s ->
                s.substring(0..2) to Node(s.substring(3..5), s.substring(6..8))
            }
        }

    fun instructionsSequence(input: List<String>): Sequence<Char> {
        val instr = input.first().splitToSequence(" ").first()
        var itr = instr.iterator()
        return generateSequence {
            if (!itr.hasNext())
                itr = instr.iterator()

            itr.next()
        }
    }

    fun nodeSequence(initial: String, network: Map<String, Node>, instructions: Sequence<Char>): Sequence<String> {
        var currentNode = initial
        val iter = instructions.iterator()

        return generateSequence {
            currentNode = when (iter.next()) {
                'L' -> network[currentNode]!!.left
                'R' -> network[currentNode]!!.right
                else -> throw IllegalStateException("unknown direction")
            }
            currentNode
        }
    }

    fun part1(input: List<String>) =
        nodeSequence("AAA", readNetwork(input), instructionsSequence(input)).takeWhile { it != "ZZZ" }.count() + 1

    fun part2(input: List<String>): Long {
        val network = readNetwork(input)
        val initialNodes = network.keys.filter { it.last() == 'A' }

        return findLCMOfListOfNumbers(initialNodes.map { initial ->
            nodeSequence(initial, network, instructionsSequence(input)).takeWhile { it.last() != 'Z' }.count() + 1L
        })
    }

    val testInput = readInput("Day08_test")
    part1(testInput).println()
    check(part1(testInput) == 6)

    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
