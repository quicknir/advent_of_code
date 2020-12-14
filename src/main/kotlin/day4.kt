package day4

import utils.*


val fields = listOf<Pair<String, (String) -> Boolean>>(
    "byr" to { it.toInt() in 1920..2002 },
    "iyr" to { it.toInt() in 2010..2020 },
    "eyr" to { it.toInt() in 2020..2030 },
    "hgt" to { h ->
        h.popSuffix("cm")?.let { it.toInt() in 150..193 } ?: h.popSuffix("in")?.let { it.toInt() in 59..76 } ?: false
    },
    "hcl" to { it[0] == '#' && it.substring(1).all { c -> c.isDigit() || c in 'a'..'f' } },
    "ecl" to { it in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") },
    "pid" to { it.length == 9 && it.all { c -> c.isDigit() } })

fun getInput() = (aocDataDir / "day4.txt").useChunkedLines { chunks -> chunks.map { passport ->
        passport
            .map { it.split(" ") }
            .flatten()
            .associate { it.split(":").let { (k, v) -> k to v } }
    }.toList()
}

fun Map<String, String>.passportValid1() = fields.all { it.first in this }

fun part1() = getInput().count { p -> fields.all { it.first in p } }.also { println(it) }
fun part2() = getInput()
    .count { p -> fields.all { f -> p[f.first]?.let { f.second(it) } ?: false } }
    .also { println(it) }

fun main() {
    part1()
}