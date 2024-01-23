import kotlin.math.abs

fun main() {
    data class Point(val x: Long, val y: Long)

    fun remapGalaxies(input: List<String>, galaxies: List<Point>, expansionFactor: Long): List<Point> {
        val isBlankHorz = input.indices.map { y -> input[y].all { it == '.' } }
        val isBlankVert = input[0].indices.map { x -> input.indices.map { y -> input[y][x] }.all { it == '.' } }

        fun countBlank(ix: Int, isBlank: List<Boolean>) = (0 until ix).count { isBlank[it] }
        fun remap(ix: Long, isBlank: List<Boolean>) = countBlank(ix.toInt(), isBlank) * (expansionFactor - 1) + ix

        return galaxies.map { pt -> Point(remap(pt.x, isBlankVert), remap(pt.y, isBlankHorz)) }
    }

    fun findGalaxyLocations(map: List<String>) = map.indices.flatMap { y ->
        map[0].indices.map { x -> if (map[y][x] == '#') Point(x.toLong(), y.toLong()) else null }
    }.filterNotNull()

    fun findGalaxyPairs(gx: List<Point>) =
        (0 until gx.size - 1).flatMap { start -> (start + 1 until gx.size).map { end -> gx[start] to gx[end] } }

    fun distanceBetween(a: Point, b: Point) = abs(a.x - b.x) + abs(a.y - b.y)

    fun calculate(input: List<String>, expansionFactor: Long) =
        findGalaxyPairs(remapGalaxies(input, findGalaxyLocations(input), expansionFactor)).sumOf {
            distanceBetween(it.first, it.second)
        }

    fun part1(input: List<String>) = calculate(input, 2)

    fun part2(input: List<String>) = calculate(input, 1000000)

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
