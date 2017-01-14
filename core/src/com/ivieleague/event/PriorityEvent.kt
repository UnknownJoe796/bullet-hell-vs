package com.ivieleague.event

import com.ivieleague.disposable.listen
import com.lightningkite.kotlin.Disposable

/**
 * Created by josep on 1/7/2017.
 */
interface PriorityHolder : Comparable<PriorityHolder> {
    val priority: Int
    override fun compareTo(other: PriorityHolder): Int = priority.compareTo(other.priority)
}

interface PriorityListener0 : () -> Unit, PriorityHolder
interface PriorityListener1<A> : (A) -> Unit, PriorityHolder
interface PriorityListener2<A, B> : (A, B) -> Unit, PriorityHolder
interface PriorityListener3<A, B, C> : (A, B, C) -> Unit, PriorityHolder

inline fun MutableCollection<Disposable>.listen(
        collection: MutableCollection<PriorityListener0>,
        priority: Int,
        crossinline lambda: () -> Unit
) {
    val item = object : PriorityListener0 {
        override val priority: Int = priority
        override fun invoke() = lambda()
    }
    listen(collection, item)
}

inline fun <A> MutableCollection<Disposable>.listen(
        collection: MutableCollection<PriorityListener1<A>>,
        priority: Int,
        crossinline lambda: (A) -> Unit
) {
    val item = object : PriorityListener1<A> {
        override val priority: Int = priority
        override fun invoke(a: A) = lambda(a)
    }
    listen(collection, item)
}