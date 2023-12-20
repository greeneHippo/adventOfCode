package nl.groen.previousDays.day7

import org.apache.commons.lang3.StringUtils

open class HandBasic(val bid: Int, val cards: List<String>) {
    var rank: Int = 0
    var type: Type? = null
    var order = determineOrder()

    open fun determineType5Cards(): Type {
        return determineType5Cards(cards.groupingBy { it[0].uppercase() }.eachCount())
    }

    open fun determineType5Cards(cardsGrouped: Map<String, Int>): Type {

        if (cardsGrouped.count() == 1) {
            return Type.FIVE_OF_A_KIND
        }

        if (cardsGrouped.count() == 2) {
            return if (cardsGrouped.any { it.value == 4 }) {
                Type.FOUR_OF_A_KIND
            } else {
                Type.FULL_HOUSE
            }
        }

        if (cardsGrouped.count() == 3) {
            return if (cardsGrouped.any { it.value == 3 }) {
                Type.THREE_OF_A_KIND
            } else {
                Type.TWO_PAIR
            }
        }

        if (cardsGrouped.count() == 4) {
            return Type.ONE_PAIR
        }

        return Type.HIGH_CARD
    }

    open fun determineType4Cards(cardsGrouped: Map<String, Int>): Type {

        if (cardsGrouped.count() == 1) {
            return Type.FOUR_OF_A_KIND
        }

        if (cardsGrouped.count() == 2) {
            return if (cardsGrouped.any { it.value == 3 }) {
                Type.THREE_OF_A_KIND
            } else {
                Type.TWO_PAIR
            }
        }

        if (cardsGrouped.count() == 3) {
            return Type.ONE_PAIR
        }

        return Type.HIGH_CARD
    }

    open fun determineType3Cards(cardsGrouped: Map<String, Int>): Type {

        if (cardsGrouped.count() == 1) {
            return Type.THREE_OF_A_KIND
        }

        if (cardsGrouped.count() == 2) {
            return Type.ONE_PAIR
        }

        return Type.HIGH_CARD
    }

    open fun determineType2Cards(cardsGrouped: Map<String, Int>): Type {

        if (cardsGrouped.count() == 1) {
            return Type.ONE_PAIR
        }

        return Type.HIGH_CARD
    }

    private fun determineOrder(): Long {
        var result = 0L
        var i = 1
        for (card in cards.reversed()) {

            var strength = strength(card)
            for (j in 1..i) {
                strength *= 14L
            }
            result += strength
            i++
        }

        return result
    }

    open fun strength(card: String): Long {
        if (card[0].isDigit()) {
            return card.toLong()
        }
        if (StringUtils.equals(card, "A")) {
            return 14L
        }
        if (StringUtils.equals(card, "K")) {
            return 13L
        }
        if (StringUtils.equals(card, "Q")) {
            return 12L
        }
        if (StringUtils.equals(card, "J")) {
            return 11L
        }
        if (StringUtils.equals(card, "T")) {
            return 10L
        }

        return 0
    }
}

class HandJoker (bid: Int, cards: List<String>) : HandBasic(bid, cards) {

    override fun strength(card: String): Long {

        if (StringUtils.equals(card, "J")) {
            return 1L
        }

        return super.strength(card)
    }

    override fun determineType5Cards(): Type {

        val cardsGrouped = cards.groupingBy { it[0].uppercase() }.eachCount()

        if (cardsGrouped["J"] == null) {
            return super.determineType5Cards(cardsGrouped)
        }

        // Alleen nog situaties met Jokers
        if (cardsGrouped["J"] == 1) {
            // Er is in totaal 1 joker.
            type = super.determineType4Cards(cardsGrouped.filterNot { it.key == "J" })

            return when(type) {
                Type.HIGH_CARD -> Type.ONE_PAIR
                Type.ONE_PAIR -> Type.THREE_OF_A_KIND
                Type.TWO_PAIR -> Type.FULL_HOUSE
                Type.THREE_OF_A_KIND -> Type.FOUR_OF_A_KIND
                Type.FOUR_OF_A_KIND -> Type.FIVE_OF_A_KIND
                else -> {
                    throw RuntimeException("No digit")
                }
            }

        }

        if (cardsGrouped["J"] == 2) {
            // Er zijn in totaal 2 jokers.
            type = super.determineType3Cards(cardsGrouped.filterNot { it.key == "J" })

            return when(type) {
                Type.HIGH_CARD -> Type.THREE_OF_A_KIND
                Type.ONE_PAIR -> Type.FOUR_OF_A_KIND
                Type.THREE_OF_A_KIND -> Type.FIVE_OF_A_KIND
                else -> {
                    throw RuntimeException("No digit")
                }
            }

        }

        if (cardsGrouped["J"] == 3) {
            // Er zijn in totaal 3 jokers.
            type = super.determineType2Cards(cardsGrouped.filterNot { it.key == "J" })

            return when(type) {
                Type.HIGH_CARD -> Type.FOUR_OF_A_KIND
                Type.ONE_PAIR -> Type.FIVE_OF_A_KIND
                else -> {
                    throw RuntimeException("No digit")
                }
            }

        }

        if (cardsGrouped["J"] == 4) {
           return Type.FIVE_OF_A_KIND
        }

        if (cardsGrouped["J"] == 5) {
            return Type.FIVE_OF_A_KIND
        }

        throw RuntimeException("No digit")
    }
}
