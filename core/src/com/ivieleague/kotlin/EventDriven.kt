package com.ivieleague.kotlin

/**
 * Created by josep on 1/10/2017.
 */
inline fun MutableIterable<() -> Unit>.runAllRemoving() {
    val it = iterator()
    while (it.hasNext()) {
        it.next().invoke()
        it.remove()
    }
}