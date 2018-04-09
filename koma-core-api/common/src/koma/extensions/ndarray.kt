package koma.extensions

import koma.internal.default.utils.linearToNIdx
import koma.ndarray.NDArray

operator fun <T> NDArray<T>.get(vararg indices: Int) = getGeneric(*indices)
operator fun <T> NDArray<T>.get(vararg indices: IntRange) = getGeneric(*indices)

operator fun NDArray<Double>.get(vararg indices: Int) = getDouble(*indices)
operator fun NDArray<Double>.get(vararg indices: IntRange) = getDouble(*indices)

operator fun <T> NDArray<T>.set(vararg indices: Int, value: T) = setGeneric(indices=*indices, value=value)
operator fun <T> NDArray<T>.set(vararg indices: Int, value: NDArray<T>) = setGeneric(indices=*indices, value=value)

operator fun NDArray<Double>.set(vararg indices: Int, value: Double) = setDouble(indices=*indices, value=value)
operator fun NDArray<Double>.set(vararg indices: Int, value: NDArray<Double>) = setDouble(indices=*indices, value=value)



/**
 * Takes each element in a NDArray, passes them through f, and puts the output of f into an
 * output NDArray.
 *
 * @param f A function that takes in an element and returns an element
 *
 * @return the new NDArray after each element is mapped through f
 */
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
inline fun <T> NDArray<T>.forEachIndexedN(f: (idx: IntArray, ele: T) -> Unit)
        = this.forEachIndexed { idx, ele -> f(linearToNIdx(idx), ele) }
