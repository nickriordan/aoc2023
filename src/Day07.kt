enum class HandStrength(val value: Int) {
    FIVE_OF_A_KIND(6),
    FOUR_OF_A_KIND(5),
    FULL_HOUSE(4),
    THREE_OF_A_KIND(3),
    TWO_PAIR(2),
    ONE_PAIR(1),
    HIGH_CARD(0)
}

fun main() {
    abstract class Card : Comparable<Card> {
        abstract val card: Char
        open val value: Int
            get() = when {
                card == 'T' -> 10
                card == 'J' -> 11
                card == 'Q' -> 12
                card == 'K' -> 13
                card == 'A' -> 14
                card.isDigit() -> card - '0'
                else -> throw IllegalArgumentException("Invalid card: $card")
            }

        override fun compareTo(other: Card) = value - other.value
    }

    data class NormalCard(override val card: Char) : Card()

    data class JokerPackCard(override val card: Char) : Card() {
        override val value: Int
            get() = if (card == 'J') 1 else super.value
    }

    abstract class Hand : Comparable<Hand> {
        abstract val cards: List<Card>
        abstract val bid: Int
        abstract val handStrength: HandStrength

        fun countGroupsOf(n: Int) = cards.groupBy { it }.filterValues { it.size == n }.size

        override fun compareTo(other: Hand): Int = (handStrength.value - other.handStrength.value).let { r ->
            if (r == 0) cards.zip(other.cards).map { (t, o) -> t.compareTo(o) }.first { it != 0 } else r
        }
    }

    data class NormalPackHand(override val cards: List<Card>, override val bid: Int) : Hand() {
        constructor(cards: String, bid: Int) : this(cards.map { NormalCard(it) }, bid)

        override val handStrength: HandStrength =
            when {
                countGroupsOf(5) > 0 -> HandStrength.FIVE_OF_A_KIND
                countGroupsOf(4) > 0 -> HandStrength.FOUR_OF_A_KIND
                countGroupsOf(3) == 1 && countGroupsOf(2) == 1 -> HandStrength.FULL_HOUSE
                countGroupsOf(3) == 1 -> HandStrength.THREE_OF_A_KIND
                countGroupsOf(2) == 2 -> HandStrength.TWO_PAIR
                countGroupsOf(2) == 1 -> HandStrength.ONE_PAIR
                else -> HandStrength.HIGH_CARD
            }

    }

    data class JokerPackHand(override val cards: List<Card>, override val bid: Int) : Hand() {
        constructor(cards: String, bid: Int) : this(cards.map { JokerPackCard(it) }, bid)

        private val countJacks = cards.count { it == JokerPackCard('J') }

        override val handStrength: HandStrength =
            when {
                countGroupsOf(5) > 0 -> HandStrength.FIVE_OF_A_KIND
                countGroupsOf(4) == 1 && countJacks == 1 -> HandStrength.FIVE_OF_A_KIND
                countGroupsOf(3) == 1 && countJacks == 2 -> HandStrength.FIVE_OF_A_KIND
                countGroupsOf(2) == 1 && countJacks == 3 -> HandStrength.FIVE_OF_A_KIND
                countJacks == 4 -> HandStrength.FIVE_OF_A_KIND

                countGroupsOf(4) == 1 && countJacks == 0 -> HandStrength.FOUR_OF_A_KIND
                countGroupsOf(3) == 1 && countJacks == 1 -> HandStrength.FOUR_OF_A_KIND
                countGroupsOf(2) == 2 && countJacks == 2 -> HandStrength.FOUR_OF_A_KIND
                countGroupsOf(2) == 0 && countJacks == 3 -> HandStrength.FOUR_OF_A_KIND

                countGroupsOf(3) == 1 && countGroupsOf(2) == 1 -> HandStrength.FULL_HOUSE
                countGroupsOf(2) == 2 && countJacks == 1 -> HandStrength.FULL_HOUSE

                countGroupsOf(3) == 1 -> HandStrength.THREE_OF_A_KIND
                countGroupsOf(2) == 1 && countJacks == 1 -> HandStrength.THREE_OF_A_KIND
                countGroupsOf(2) == 1 && countGroupsOf(3) == 0 && countJacks == 2 -> HandStrength.THREE_OF_A_KIND

                countGroupsOf(2) == 2 -> HandStrength.TWO_PAIR
                countGroupsOf(2) == 1 && countJacks == 1 -> HandStrength.TWO_PAIR

                countGroupsOf(2) == 1 -> HandStrength.ONE_PAIR
                countJacks == 1 -> HandStrength.ONE_PAIR

                else -> HandStrength.HIGH_CARD
            }

    }

    fun calculate(input: List<String>, f: (String, Int) -> Hand) =
        input.map { line ->
            line.split(" ").let { (cards, bid) -> f(cards, bid.toInt()) }
        }.sorted().foldIndexed(0) { ix, r, hand -> r + hand.bid * (ix + 1) }

    fun part1(input: List<String>) = calculate(input) { cards, bid -> NormalPackHand(cards, bid) }

    fun part2(input: List<String>) = calculate(input) { cards, bid -> JokerPackHand(cards, bid) }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
