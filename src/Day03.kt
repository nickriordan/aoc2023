fun main() {
    data class Part(val x: Int, val y: Int, val symbol: Char)

    data class PartNumber(val xRange: IntRange, val yRange: IntRange, val v: Int) {
        fun isAdjacentTo(part: Part) = part.x in xRange && part.y in yRange
    }

    data class Schematic(val parts: List<Part> = emptyList(), val partNumbers: List<PartNumber> = emptyList()) {
        fun addNumber(x: Int, y: Int, s: String) =
            Schematic(parts, partNumbers + PartNumber((x - 1..x + s.length), y - 1..y + 1, s.toInt()))

        fun addPart(x: Int, y: Int, symbol: Char) = Schematic(parts + Part(x, y, symbol), partNumbers)

        fun gears() = parts.filter { it.symbol == '*' }
            .map { part -> numbersAdjacentTo(part) }
            .filter { it.count() > 1 }

        fun numbersAdjacentTo(part: Part) = partNumbers.filter { partNumber -> partNumber.isAdjacentTo(part) }
    }

    fun readSchematic(input: List<String>) = input.foldIndexed(Schematic()) { y, schematic, line ->
        fun readLine(schematic: Schematic, s: String): Schematic = s.dropWhile { it == '.' }.let { remain ->
            remain.takeWhile { it.isDigit() }.takeIf { it.isNotEmpty() }?.let { s ->
                readLine(schematic.addNumber(line.length - remain.length, y, s), remain.drop(s.length))
            } ?: remain.elementAtOrNull(0)?.let { ch ->
                readLine(schematic.addPart(line.length - remain.length, y, ch), remain.drop(1))
            } ?: schematic
        }
        readLine(schematic, line)
    }

    fun part1(input: List<String>) = readSchematic(input).let { schematic ->
        schematic.partNumbers.filter { num -> schematic.parts.any { part -> num.isAdjacentTo(part) } }.sumOf { it.v }
    }

    fun part2(input: List<String>) = readSchematic(input).let { schematic ->
        schematic.gears().sumOf { it.map { part -> part.v }.reduce { ratio, part -> ratio * part } }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
