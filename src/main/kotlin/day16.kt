package day16

import utils.*

data class Restriction(val name: String, val first: IntRange, val second: IntRange)

fun Restriction.isValid(int: Int) = int in first || int in second

data class InputData(
    val restrictions: List<Restriction>,
    val myTicket: List<Int>,
    val nearbyTickets: List<List<Int>>
)

fun getInput(): InputData {
    val data = (aocDataDir / "day16.txt").readChunkedLines()

    val restrictions = data[0].map { line ->
        val (name, rest) = line.split(": ")
        val restrList = rest.split(" or ").map {
            val (lower, upper) = it.split("-")
            IntRange(lower.toInt(), upper.toInt())
        }
        Restriction(name, restrList[0], restrList[1])
    }

    val myTicket = data[1][1].split(",").map { it.toInt() }
    val nearbyTickets = data[2].subList(1, data[2].size).map { line ->
        line.split(",").map { it.toInt() }
    }
    return InputData(restrictions, myTicket, nearbyTickets)
}

fun part1(): Int {
    val input = getInput()
    return input.nearbyTickets.asSequence().flatten().filter { ticketValue ->
        !input.restrictions.any { it.isValid(ticketValue) }
    }.sum()
}

fun part2(): Long {
    val input = getInput()
    val validTickets = (input.nearbyTickets.asSequence().filter { ticket ->
        ticket.all { ticketValue ->
            input.restrictions.any { it.isValid(ticketValue) }
        }
    } + sequenceOf(input.myTicket)).toList()

    val numFields = input.myTicket.size
    val possibilities = List(numFields) { (0 until numFields).toMutableSet() }

    for (ticket in validTickets) {
        for ((ticketIndex, ticketValue) in ticket.withIndex()) {
            for (restrictionIndex in possibilities[ticketIndex].toList()) {
                if (!input.restrictions[restrictionIndex].isValid(ticketValue)) {
                    possibilities[ticketIndex].remove(restrictionIndex)
                }
            }
        }
    }
    while (true) {
        val determined = possibilities.filter { it.size == 1 }
        if (determined.size == possibilities.size) break
        for (d in determined) {
            for (p in possibilities) {
                if (!(d === p)) p.remove(d.first())
            }
        }
    }
    val timesFields = (0 until numFields).filter { ticketIndex ->
        input.restrictions[possibilities[ticketIndex].first()].name.startsWith("departure")
    }.map { input.myTicket[it] }
    assert(timesFields.size == 6)
    return timesFields.fold(1L) { acc, i -> acc * i.toLong() }
}

fun main() {
    println(part2())
}
