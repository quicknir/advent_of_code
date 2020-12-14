package day9

import utils.*

fun getData() = (aocDataDir / "day9.txt").readLines().map { it.toLong() }

fun Map<Long, Long>.checkSumPair(sum: Long): Pair<Long, Long>? {
    if (sum % 2 == 0L && getOrDefault(sum / 2, 0) >= 2) {
        return Pair(sum/2, sum/2)
    }
    return keys.find { (it <= (sum-1) / 2) && (getOrDefault(sum-it, 0) >=1) }?.let {
        Pair(it, sum-it)
    }
}

fun part1(): Long {
    val data = getData()
    val trailingCounts = mutableMapOf<Long, Long>().apply {
        data.subList(0, 25).forEach { int -> update(int) { (it ?: 0) + 1 } }
    }
    val trailingDeque = ArrayDeque(data.subList(0, 25))
    for (element in data.subList(25, data.size)) {
        if (trailingCounts.checkSumPair(element) == null) {
            return element
        }
        val remove = trailingDeque.removeFirst()
        trailingCounts.update(remove) { it!! - 1 }
        trailingDeque.add(element)
        trailingCounts.update(element) { (it ?: 0) + 1 }
    }
    throw Exception("oops")
}

fun part2(): Long {
    val targetSum = 31161678L
    val data = getData()
    var startIndex = 0
    var endIndex = 2
    var curSum = getData().subList(0, 2).sum()
    while (true) {
        if (curSum == targetSum) {
            val subList = data.subList(startIndex, endIndex)
            return subList.minOrNull()!! + subList.maxOrNull()!!
        }
        if ((startIndex - endIndex) == 2 || curSum < targetSum) {
            curSum += data[endIndex]
            endIndex++
            continue
        }
        curSum -= data[startIndex]
        startIndex++
    }
}

fun main() {
    println(part2())
    print("hello".map { if (it == 'h') 'x' else it }.joinToString(separator=""))
}