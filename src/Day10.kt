fun main() {
    data class Point(val x: Int, val y: Int)

    data class Pipe(val ch: Char, val pt: Point)

    class Pipes(private val pipes: List<List<Char>>) : Iterable<Pipe> {
        private val validNorth = listOf("S|", "S7", "SF", "||", "|7", "|F", "L|", "L7", "LF", "J|", "J7", "JF")
        private val validSouth = listOf("S|", "SL", "SJ", "||", "|L", "|J", "7|", "7L", "7J", "F|", "FL", "FJ")
        private val validEast = listOf("S-", "SJ", "S7", "--", "-J", "-7", "L-", "LJ", "L7", "F-", "FJ", "F7")
        private val validWest = listOf("S-", "SL", "SF", "--", "-L", "-F", "J-", "JL", "JF", "7-", "7L", "7F")

        private fun Point.north() = if (y > 0) Point(x, y - 1) else null
        private fun Point.south() = if (y < pipes.size - 1) Point(x, y + 1) else null
        private fun Point.east() = if (x < pipes[0].size - 1) Point(x + 1, y) else null
        private fun Point.west() = if (x > 0) Point(x - 1, y) else null
        private fun Point.checkNot(other: Point?) = if (this == other) null else this

        private fun path(curr: Point, next: Point) = "${pipes[curr.y][curr.x]}${pipes[next.y][next.x]}"
        private fun moveNorth(pt: Point) = pt.north()?.let { if (validNorth.contains(path(pt, it))) it else null }
        private fun moveSouth(pt: Point) = pt.south()?.let { if (validSouth.contains(path(pt, it))) it else null }
        private fun moveEast(pt: Point) = pt.east()?.let { if (validEast.contains(path(pt, it))) it else null }
        private fun moveWest(pt: Point) = pt.west()?.let { if (validWest.contains(path(pt, it))) it else null }

        private val startPoint = pipes.indices.flatMap { y -> pipes[y].indices.map { x -> Point(x, y) } }
            .find { pipes[it.y][it.x] == 'S' }!!

        private val startCh: Char = when {
            moveNorth(startPoint) != null && moveSouth(startPoint) != null -> '|'
            moveNorth(startPoint) != null && moveEast(startPoint) != null -> 'L'
            moveNorth(startPoint) != null && moveWest(startPoint) != null -> 'J'
            moveSouth(startPoint) != null && moveEast(startPoint) != null -> 'F'
            moveSouth(startPoint) != null && moveWest(startPoint) != null -> '7'
            moveSouth(startPoint) != null && moveWest(startPoint) != null -> '-'
            else -> throw IllegalStateException()
        }

        fun cleanMap(): List<String> {
            val m = associate { it.pt to it.ch }.withDefault { '.' }
            return pipes.indices.map { y ->
                pipes[y].indices.map { x -> m.getValue(Point(x, y)) }.joinToString("")
            }
        }

        override fun iterator(): Iterator<Pipe> = object : Iterator<Pipe> {
            private var lastPoint: Point? = null
            private var currentPoint: Point? = startPoint

            private fun pipeAt(pt: Point) = if (pt == startPoint) startCh else pipes[pt.y][pt.x]

            override fun hasNext(): Boolean = currentPoint != null

            override fun next() = Pipe(pipeAt(currentPoint!!), currentPoint!!).also {
                val next =
                    moveNorth(currentPoint!!)?.checkNot(lastPoint) ?: moveSouth(currentPoint!!)?.checkNot(lastPoint)
                    ?: moveEast(currentPoint!!)?.checkNot(lastPoint) ?: moveWest(currentPoint!!)?.checkNot(lastPoint)

                lastPoint = currentPoint
                currentPoint = next
            }
        }
    }

    fun part1(input: List<String>) = Pipes(input.map { line -> line.map { it } }).count() / 2

    fun part2(input: List<String>) = Pipes(input.map { line -> line.map { it } }).cleanMap().sumOf { line ->
        data class Data(val inside: Boolean = false, val startCorner: Char? = null, val countInside: Int = 0)
        line.fold(Data()) { acc, ch ->
            when (ch) {
                '|' -> acc.copy(inside = !acc.inside)
                'L', 'F' -> acc.copy(startCorner = ch)
                'J' -> acc.copy(startCorner = null, inside = if (acc.startCorner == 'F') !acc.inside else acc.inside)
                '7' -> acc.copy(startCorner = null, inside = if (acc.startCorner == 'L') !acc.inside else acc.inside)
                '.' -> if (acc.inside) acc.copy(countInside = acc.countInside + 1) else acc
                else -> acc
            }
        }.countInside
    }

    check(part1(readInput("Day10_test")) == 8)
    check(part2(readInput("Day10_test2")) == 10)
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
