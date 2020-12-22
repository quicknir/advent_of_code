package day19

import utils.*

sealed class RuleComponent
data class RuleIndex(val index: Int) : RuleComponent()
data class RuleChar(val char: Char) : RuleComponent()
data class Rule(val subrules: List<List<RuleComponent>>)

fun getInput(): Pair<MutableMap<Int, Rule>, List<String>> {
    val chunks = (aocDataDir / "day19.txt").readChunkedLines()
    return chunks[0].associateTo(mutableMapOf()) { line ->
        val (ruleNumber, ruleString) = line.split(": ")
        val subRules = ruleString.split(" | ").map { subrule ->
            subrule.split(" ").map { rule ->
                if (rule.startsWith('"')) {
                    RuleChar(rule[1])
                } else {
                    RuleIndex(rule.toInt())
                }
            }
        }
        ruleNumber.toInt() to Rule(subRules)
    } to chunks[1]
}

fun Map<Int, Rule>.check(ruleIndex: Int, string: String): Set<String> =
    getValue(ruleIndex).subrules.asSequence().map { subrule ->
        subrule.fold(setOf(string)) { acc, r ->
            if (acc.isEmpty()) {
                return@fold acc
            }
            when (r) {
                is RuleChar -> {
                    acc.asSequence().mapNotNull { it.popPrefix(r.char.toString()) }.toSet()
                }
                is RuleIndex -> {
                    acc.asSequence().map { check(r.index, it) }.flatten().toSet()
                }
            }
        }
    }.flatten().toSet()

fun part1() = getInput().let { (graph, inputs) -> inputs.count { "" in graph.check(0, it) } }

fun part2() = getInput().let { (graph, inputs) ->
    graph[8] = Rule(listOf(listOf(RuleIndex(42)), listOf(RuleIndex(42), RuleIndex(8))))
    graph[11] = Rule(
        listOf(
            listOf(RuleIndex(42), RuleIndex(31)),
            listOf(RuleIndex(42), RuleIndex(11), RuleIndex(31))
        )
    )
    inputs.count { "" in graph.check(0, it) }
}

fun main() {
    println(part2())
}
