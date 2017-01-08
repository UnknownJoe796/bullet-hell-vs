package com.ivieleague.event

import com.lightningkite.kotlin.lifecycle.LifecycleConnectable
import com.lightningkite.kotlin.lifecycle.listen

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

inline fun LifecycleConnectable.listen(
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

inline fun <A> LifecycleConnectable.listen(
        collection: MutableCollection<PriorityListener1<A>>,
        priority: Int,
        crossinline lambda: (A) -> Unit
) {
    val item = object : PriorityListener1<A> {
        override val priority: Int = priority
        override fun invoke(item: A) = lambda(item)
    }
    listen(collection, item)
}

inline fun <A, B> LifecycleConnectable.listen(
        collection: MutableCollection<PriorityListener2<A, B>>,
        priority: Int,
        crossinline lambda: (A, B) -> Unit
) {
    val item = object : PriorityListener2<A, B> {
        override val priority: Int = priority
        override fun invoke(a: A, b: B) = lambda(a, b)
    }
    listen(collection, item)
}

inline fun <A, B, C> LifecycleConnectable.listen(
        collection: MutableCollection<PriorityListener3<A, B, C>>,
        priority: Int,
        crossinline lambda: (A, B, C) -> Unit
) {
    val item = object : PriorityListener3<A, B, C> {
        override val priority: Int = priority
        override fun invoke(a: A, b: B, c: C) = lambda(a, b, c)
    }
    listen(collection, item)
}