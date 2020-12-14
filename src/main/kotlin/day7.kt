package day7

import utils.*

data class ContainedBag(val num: Int, val name: String)
data class Rule(val containing: String, val contained: List<ContainedBag>)

fun String.toRule(): Rule {
    val (containing, contained) = split(" contain ")
    val containingBag = containing.popSuffix(" bags")!!
    if (contained == "no other bags.") return Rule(containingBag, emptyList())
    val containedBags = contained.splitDropEmpty(", ", ".").map {
        val (num, name) = (it.popSuffix(" bags") ?: it.popSuffix(" bag")!!).split(" ", limit=2)
        ContainedBag(num.toInt(), name)
    }
    return Rule(containingBag, containedBags)
}

fun getData() = (aocDataDir / "day7.txt").useLines { lines -> lines.map { it.toRule() }.toList() }

fun addParents(parentGraph: Map<String, List<String>>, key: String, alreadyFound: MutableSet<String>) {
    for (bag in parentGraph[key] ?: return) {
        if (bag in alreadyFound) continue
        alreadyFound.add(bag)
        addParents(parentGraph, bag, alreadyFound)
    }
}

fun part1(): Int {
    val parentMap: Map<String, List<String>> = mutableMapOf<String, MutableList<String>>().apply {
        getData().forEach { rule ->
            rule.contained.forEach {
                getOrPut(it.name) { mutableListOf() }.add(rule.containing)
            }
        }
    }
    val bagSet = mutableSetOf<String>()
    addParents(parentMap, "shiny gold", bagSet)
    return bagSet.size
}

fun sumChildren(graph: Map<String, List<ContainedBag>>, key: String): Int {
    return graph.getValue(key)
        .map { it.num * (1 + sumChildren(graph, it.name)) }
        .sum()
}

fun part2(): Int {
    val graph = getData().associate { it.containing to it.contained }
    return sumChildren(graph, "shiny gold")
}

fun main() {
    println(part2())
}