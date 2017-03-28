package com.ivieleague.event

import com.ivieleague.disposable.listen
import com.lightningkite.kotlin.Disposable

/**
 * Created by josep on 1/7/2017.
 */
interface PriorityListener : Comparable<PriorityListener> {
    val priority: Int
    override fun compareTo(other: PriorityListener): Int = priority.compareTo(other.priority)

    companion object{
        inline fun make(priority: Int, crossinline lambda: () -> Unit) = object: PriorityListener0{
            override val priority: Int = priority
            override fun invoke() = lambda.invoke()
        }
        inline fun <A> make(priority: Int, crossinline lambda: (A) -> Unit) = object: PriorityListener1<A>{
            override val priority: Int = priority
            override fun invoke(a:A) = lambda.invoke(a)
        }
        inline fun <A, B> make(priority: Int, crossinline lambda: (A, B) -> Unit) = object: PriorityListener2<A, B>{
            override val priority: Int = priority
            override fun invoke(a:A, b:B) = lambda.invoke(a, b)
        }
    }
}

interface PriorityListener0 : () -> Unit, PriorityListener
interface PriorityListener1<A> : (A) -> Unit, PriorityListener
interface PriorityListener2<A, B> : (A, B) -> Unit, PriorityListener
interface PriorityListener3<A, B, C> : (A, B, C) -> Unit, PriorityListener

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
inline fun <A, B> MutableCollection<Disposable>.listen(
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