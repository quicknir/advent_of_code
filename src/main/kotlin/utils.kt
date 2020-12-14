package utils

import java.io.File

val aocDataDir = File("C:\\Users\\quick\\Downloads\\advent_of_code")

operator fun File.div(s: String) = resolve(s)

fun <K, V> MutableMap<K, V>.update(k: K, transform: (V?) -> V) {
    this[k] = transform(this[k])
}

fun String.popSuffix(suffix: String) = if (endsWith(suffix)) removeSuffix(suffix) else null

fun String.splitDropEmpty(vararg delimiters: String, ignoreCase: Boolean = false, limit: Int = 0) =
    split(*delimiters, ignoreCase = ignoreCase, limit = limit).filter { it.isNotEmpty() }

fun <T, R> Sequence<T>.chunkedBy(key: (T) -> R) = sequence {
    val iter = this@chunkedBy.iterator()
    if (!iter.hasNext()) return@sequence
    var currentList = mutableListOf(iter.next())
    var currentKey = key(currentList.first())

    for (t in iter) {
        val nextKey = key(t)
        if (nextKey == currentKey) {
            currentList.add(t)
            continue
        }
        yield(currentKey to currentList)
        currentKey = nextKey
        currentList = mutableListOf(t)
    }
    yield(currentKey to currentList)
}

fun <T, R> Iterable<T>.chunkedBy(key: (T) -> R) = asSequence().chunkedBy(key).toList()

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

fun main() {
    listOf("hello", "there", "", "bye", "there").asSequence().chunkedByEmpty().toList().also {println(it)}
}