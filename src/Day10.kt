fun main() {
    data class Point(val x: Int, val y: Int)

    data class Pipe(val ch: Char, val pt: Point)

    class PipeIterator(private val pipes: List<List<Char>>) : Iterator<Pipe> {
        private val validNorth = listOf("S|", "S7", "SF", "||", "|7", "|F", "L|", "L7", "LF", "J|", "J7", "JF")
        private val validSouth = listOf("S|", "SL", "SJ", "||", "|L", "|J", "7|", "7L", "7J", "F|", "FL", "FJ")
        private val validEast = listOf("S-", "SJ", "S7", "--", "-J", "-7", "L-", "LJ", "L7", "F-", "FJ", "F7")
        private val validWest = listOf("S-", "SL", "SF", "--", "-L", "-F", "J-", "JL", "JF", "7-", "7L", "7F")

        private fun Point.north() = if (y > 0) Point(x, y - 1) else null
        private fun Point.south() = if (y < pipes.size - 1) Point(x, y + 1) else null
        private fun Point.east() = if (x < pipes[0].size - 1) Point(x + 1, y) else null
        private fun Point.west() = if (x > 0) Point(x - 1, y) else null
        private fun Point.checkNotLast() = if (this == lastPoint) null else this

        private fun pipeAt(pt: Point) = pipes[pt.y][pt.x]
        private fun path(curr: Point, next: Point) = "${pipeAt(curr)}${pipeAt(next)}"
        private fun moveNorth(pt: Point) = pt.north()?.let { if (validNorth.contains(path(pt, it))) it else null }
        private fun moveSouth(pt: Point) = pt.south()?.let { if (validSouth.contains(path(pt, it))) it else null }
        private fun moveEast(pt: Point) = pt.east()?.let { if (validEast.contains(path(pt, it))) it else null }
        private fun moveWest(pt: Point) = pt.west()?.let { if (validWest.contains(path(pt, it))) it else null }

        private var lastPoint: Point? = null
        private var currentPoint: Point? =
            pipes.indices.flatMap { y -> pipes[y].indices.map { x -> Point(x, y) } }.find { pipes[it.y][it.x] == 'S' }!!

        override fun hasNext(): Boolean = currentPoint != null

        override fun next(): Pipe {
            val next =
                moveNorth(currentPoint!!)?.checkNotLast() ?: moveSouth(currentPoint!!)?.checkNotLast()
                ?: moveEast(currentPoint!!)?.checkNotLast() ?: moveWest(currentPoint!!)?.checkNotLast()

            lastPoint = currentPoint
            currentPoint = next

            return Pipe(pipeAt(lastPoint!!), lastPoint!!)
        }
    }

    fun part1(input: List<String>) = PipeIterator(input.map { line -> line.map { it } }).asSequence().count() / 2

    fun part2(input: List<String>) = input.size

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
