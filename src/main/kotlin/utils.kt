package utils

import java.io.File

val aocDataDir = File("C:\\Users\\quick\\Downloads\\advent_of_code")

operator fun<R> R.rem(transform: R.() -> R) = this.transform()

operator fun File.div(s: String) = resolve(s)

fun <K, V> MutableMap<K, V>.update(k: K, transform: (V?) -> V) = set(k, transform(get(k)))

fun <K, V> MutableMap<K, V>.putAndDefault(key: K, value: V) = put(key, value) ?: value

fun String.popSuffix(suffix: String) = if (endsWith(suffix)) removeSuffix(suffix) else null

fun String.splitDropEmpty(vararg delimiters: String, ignoreCase: Boolean = false, limit: Int = 0) =
    split(*delimiters, ignoreCase = ignoreCase, limit = limit).filter { it.isNotEmpty() }

fun Sequence<String>.chunkedByEmpty(): Sequence<List<String>> = sequence {
    var currentList = mutableListOf<String>()
    for (x in this@chunkedByEmpty) {
        if (x != "") {
            currentList.add(x)
            continue
        }
        yield(currentList)
        currentList = mutableListOf<String>()
    }
    yield(currentList)
}

inline fun <R> File.useChunkedLines(transform: (Sequence<List<String>>) -> R) =
    useLines { transform(it.chunkedByEmpty()) }

fun File.readChunkedLines() = useLines { it.asSequence().chunkedByEmpty().toList() }

data class Point(val i: Int, val j: Int)

operator fun Point.plus(p: Point) = Point(i + p.i, j + p.j)
operator fun Int.times(p: Point) = Point(this * p.i, this * p.j)
