package day15

val input = listOf(8,0,17,4,1,12)

fun run(lastTurn: Int): Int {
    val state = mutableMapOf<Int, Int>().also { map ->
        input.subList(0, input.size-1).withIndex().associateTo(map) { it.value to it.index+1 }
    }
    var lastNumber = input.last()

    for (turn in (input.size) until lastTurn) {
        val curNumber = turn - (state.put(lastNumber, turn) ?: turn)
        lastNumber = curNumber
    }

    return lastNumber
}

fun part1() = run(2020)
fun part2() = run(30000000)

fun main() {
    println(part2())
}
