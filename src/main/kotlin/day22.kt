package day22

import utils.*

typealias Deck = ArrayDeque<Long>

fun getInput() = (aocDataDir / "day22.txt").readChunkedLines().map { chunk ->
    Deck(chunk.subList(1, chunk.size).map { it.toLong() })
}

fun Deck.computeScore() = reversed().withIndex().sumOf { (it.index + 1) * it.value }

fun part1(): Long {
    val (player1, player2) = getInput()
    while (player1.isNotEmpty() && player2.isNotEmpty()) {
        val (biggerDeck, smallerDeck) = if (player1.first() > player2.first())
            player1 to player2 else player2 to player1
        biggerDeck.addLast(biggerDeck.removeFirst())
        biggerDeck.addLast(smallerDeck.removeFirst())
    }
    val nonEmpty = if (player1.isNotEmpty()) player1 else player2
    return nonEmpty.computeScore()
}

fun recursiveCombat(player1: Deck, player2: Deck): Pair<Deck, Boolean> {
    val seen = mutableSetOf<Pair<Deck, Deck>>()
    while (player1.isNotEmpty() && player2.isNotEmpty()) {
        if (Pair(player1, player2) in seen) return player1 to true
        seen += Pair(Deck(player1), Deck(player2))
        val player1Won = if (player1.first() < player1.size && player2.first() < player2.size) {
            recursiveCombat(
                Deck(player1.subList(1, 1 + player1.first().toInt())),
                Deck(player2.subList(1, 1 + player2.first().toInt()))
            ).second
        } else player1.first() > player2.first()

        val (winDeck, loseDeck) = if (player1Won) player1 to player2 else player2 to player1
        winDeck.addLast(winDeck.removeFirst())
        winDeck.addLast(loseDeck.removeFirst())
    }
    return if (player1.isNotEmpty()) player1 to true else player2 to false
}

fun part2() = getInput().let { (p1, p2) -> recursiveCombat(p1, p2) }.first.computeScore()

fun main() {
    println(part2())
}