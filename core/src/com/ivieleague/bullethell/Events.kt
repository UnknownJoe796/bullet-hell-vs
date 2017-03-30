package com.ivieleague.bullethell

import com.lightningkite.kotlin.runAll
import java.util.*

class Events {
    private val eventListeners = HashMap<String, HashSet<(Any?) -> Unit>>()
    operator fun get(key: String): MutableCollection<(Any?) -> Unit> = eventListeners.getOrPut(key, { HashSet() })
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> get() = get(T::class.java.name) as MutableCollection<(T) -> Unit>

    inline fun <reified T : Any> dispatch(t: T) = get<T>().runAll(t)
}