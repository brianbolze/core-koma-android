/**
 * Some helper conversion functions
 */
@file:JvmName("UtilConversions")

package golem.util

/**
 * Converts a collection into a primitive DoubleArray
 */
fun fromCollection(collection: Collection<Double>): DoubleArray {
    var out = DoubleArray(collection.size)
    val it = collection.iterator()
    for (i in collection.indices) {
        out[i] = it.next()
    }
    return out
}
/**
 * Converts a collection into a primitive DoubleArray
 */
fun fromCollection(collection: Collection<Int>): IntArray {
    var out = IntArray(collection.size)
    val it = collection.iterator()
    for (i in collection.indices) {
        out[i] = it.next()
    }
    return out
}