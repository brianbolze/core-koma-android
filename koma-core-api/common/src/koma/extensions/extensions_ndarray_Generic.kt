@file:koma.internal.JvmName("NDArray")
@file:koma.internal.JvmMultifileClass

/**
 * THIS FILE IS AUTOGENERATED, DO NOT MODIFY. EDIT THE FILES IN templates/
 * AND RUN ./gradlew :codegen INSTEAD!
 */

package koma.extensions

import koma.internal.default.generated.ndarray.DefaultGenericNDArray
import koma.internal.default.utils.checkIndices
import koma.internal.default.utils.linearToNIdx
import koma.matrix.doubleFactory
import koma.ndarray.NDArray
import koma.pow
import koma.matrix.Matrix



@koma.internal.JvmName("fillT")
inline fun <T> NDArray<T>.fill(f: (idx: IntArray) -> T): NDArray<T> {
    this.forEachIndexedN { idx, ele ->
        this.set(indices=*idx, value = f(idx))
    }
    return this
}


/**
 * Takes each element in a NDArray, passes them through f, and puts the output of f into an
 * output NDArray.
 *
 * @param f A function that takes in an element and returns an element
 *
 * @return the new NDArray after each element is mapped through f
 */
@koma.internal.JvmName("mapT")
inline fun <T> NDArray<T>.map(f: (T) -> T): NDArray<T> {
    // TODO: Something better than copy here
    val out = this.copy()
    for ((idx, ele) in this.toIterable().withIndex())
        out.setLinear(idx, f(ele))
    return out
}
/**
 * Takes each element in a NDArray, passes them through f, and puts the output of f into an
 * output NDArray. Index given to f is a linear index, depending on the underlying storage
 * major dimension.
 *
 * @param f A function that takes in an element and returns an element. Function also takes
 *      in the linear index of the element's location.
 *
 * @return the new NDArray after each element is mapped through f
 */
@koma.internal.JvmName("mapIndexedT")
inline fun <T> NDArray<T>.mapIndexed(f: (idx: Int, ele: T) -> T): NDArray<T> {
    // TODO: Something better than copy here
    val out = this.copy()
    for ((idx, ele) in this.toIterable().withIndex())
        out.setLinear(idx, f(idx, ele))
    return out
}
/**
 * Takes each element in a NDArray and passes them through f.
 *
 * @param f A function that takes in an element
 *
 */
@koma.internal.JvmName("forEachT")
inline fun <T> NDArray<T>.forEach(f: (ele: T) -> Unit) {
    for (ele in this.toIterable())
        f(ele)
}
/**
 * Takes each element in a NDArray and passes them through f. Index given to f is a linear
 * index, depending on the underlying storage major dimension.
 *
 * @param f A function that takes in an element. Function also takes
 *      in the linear index of the element's location.
 *
 */
@koma.internal.JvmName("forEachIndexedT")
inline fun <T> NDArray<T>.forEachIndexed(f: (idx: Int, ele: T) -> Unit) {
    for ((idx, ele) in this.toIterable().withIndex())
        f(idx, ele)
}

// TODO: for both of these, batch compute [linearToNIdx] instead of computing for every ele

/**
 * Takes each element in a NDArray, passes them through f, and puts the output of f into an
 * output NDArray. Index given to f is the full ND index of the element.
 *
 * @param f A function that takes in an element and returns an element. Function also takes
 *      in the ND index of the element's location.
 *
 * @return the new NDArray after each element is mapped through f
 */
@koma.internal.JvmName("mapIndexedNT")
inline fun <T> NDArray<T>.mapIndexedN(f: (idx: IntArray, ele: T) -> T): NDArray<T>
        = this.mapIndexed { idx, ele -> f(linearToNIdx(idx), ele) }

/**
 * Takes each element in a NDArray and passes them through f. Index given to f is the full
 * ND index of the element.
 *
 * @param f A function that takes in an element. Function also takes
 *      in the ND index of the element's location.
 *
 */
@koma.internal.JvmName("forEachIndexedNT")
inline fun <T> NDArray<T>.forEachIndexedN(f: (idx: IntArray, ele: T) -> Unit)
        = this.forEachIndexed { idx, ele -> f(linearToNIdx(idx), ele) }


@koma.internal.JvmName("getRangesT")
operator fun <T> NDArray<T>.get(vararg indices: IntRange): NDArray<T> {
    checkIndices(indices.map { it.last }.toIntArray())
    return DefaultGenericNDArray<T>(shape = *indices
            .map { it.last - it.first + 1 }
            .toIntArray()) { newIdxs ->
        val offsets = indices.map { it.first }
        val oldIdxs = newIdxs.zip(offsets).map { it.first + it.second }
        this.getGeneric(*oldIdxs.toIntArray())
    }
}

@koma.internal.JvmName("setT")
operator fun <T> NDArray<T>.set(vararg indices: Int, value: NDArray<T>) {
    val shape = shape()
    val lastIndex = indices.mapIndexed { i, range -> range + value.shape()[i] }
    val outOfBounds = lastIndex.withIndex().any { it.value > shape()[it.index] }
    if (outOfBounds)
        throw IllegalArgumentException("NDArray with shape ${shape()} cannot be " +
                "set at ${indices.toList()} by a ${value.shape()} array " +
                "(out of bounds)")

    val offset = indices.map { it }.toIntArray()
    value.forEachIndexedN { idx, ele ->
        val newIdx = offset.zip(idx).map { it.first + it.second }.toIntArray()
        this.setGeneric(indices=*newIdx, value=ele)
    }
}


operator fun <T> NDArray<T>.get(vararg indices: Int) = getGeneric(*indices)
operator fun <T> NDArray<T>.set(vararg indices: Int, value: T) = setGeneric(indices=*indices, value=value)

