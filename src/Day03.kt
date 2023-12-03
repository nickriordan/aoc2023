fun main() {
    data class PartNumber(val xRange: IntRange, val yRange: IntRange, val partNumber: Int) {
        fun isAdjacentTo(x: Int, y: Int) = x in xRange && y in yRange
    }

    data class Part(val x: Int, val y: Int, val symbol: Char) {
        fun isGear() = symbol == '*'
    }

    data class Schematic(val parts: List<Part> = emptyList(), val partNumbers: List<PartNumber> = emptyList()) {
        fun addNumber(x: Int, y: Int, s: String) =
            Schematic(parts, partNumbers + PartNumber((x - 1..x + s.length), y - 1..y + 1, s.toInt()))

        fun addPart(x: Int, y: Int, symbol: Char) = Schematic(parts + Part(x, y, symbol), partNumbers)

        fun gears() = parts.filter { it.isGear() }

        fun partsAdjacentTo(x: Int, y: Int) = partNumbers.filter { partNumber -> partNumber.isAdjacentTo(x, y) }
    }

    fun readLine(remaining: String, x: Int, y: Int, schematic: Schematic): Schematic =
        when {
            remaining.isEmpty() -> schematic
            remaining.first() == '.' ->
                remaining.dropWhile { it == '.' }.let { s ->
                    readLine(s, x + remaining.length - s.length, y, schematic)
                }

            remaining.first().isDigit() ->
                remaining.takeWhile { it.isDigit() }.let { s ->
                    readLine(remaining.drop(s.length), x + s.length, y, schematic.addNumber(x, y, s))
                }

            else -> readLine(remaining.drop(1), x + 1, y, schematic.addPart(x, y, remaining.first()))
        }

    fun readSchematic(input: List<String>) = input.foldIndexed(Schematic()) { y, schematic, line ->
        readLine(line, 0, y, schematic)
    }

    fun part1(input: List<String>) = readSchematic(input).let { schematic ->
        schematic.partNumbers.filter { partNumber ->
            schematic.parts.any { part -> partNumber.isAdjacentTo(part.x, part.y) }
        }.sumOf { it.partNumber }
    }

    fun part2(input: List<String>) = readSchematic(input).let { schematic ->
        schematic.gears().map { part ->
            schematic.partsAdjacentTo(part.x, part.y).map { it.partNumber }
        }.filter {
            it.count() > 1
        }.sumOf { it.reduce { ratio, n -> ratio * n } }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
