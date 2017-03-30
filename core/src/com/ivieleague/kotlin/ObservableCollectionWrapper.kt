package com.ivieleague.kotlin

import com.lightningkite.kotlin.runAll
import java.util.*

/**
 * Created by josep on 1/11/2017.
 */

interface ObservableCollection<E> : MutableCollection<E> {
    val onAdd: MutableCollection<(E) -> Unit>
    val onRemove: MutableCollection<(E) -> Unit>
}

class ObservableCollectionWrapper<E>(val inner: MutableCollection<E> = HashSet()) : ObservableCollection<E> {

    override val onAdd: MutableCollection<(E) -> Unit> = ArrayList()
    override val onRemove: MutableCollection<(E) -> Unit> = ArrayList()

    override val size: Int get() = inner.size
    override fun contains(element: E): Boolean = inner.contains(element)
    override fun containsAll(elements: Collection<E>): Boolean = inner.containsAll(elements)
    override fun isEmpty(): Boolean = inner.isEmpty()

    override fun add(element: E): Boolean = inner.add(element).also {
        if (it) onAdd.runAll(element)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        var fail = false
        for (element in elements) {
            if (!add(element)) fail = true
        }
        return fail
    }

    override fun clear() {
        inner.clear()
        for (e in inner) {
            onRemove.runAll(e)
        }
    }

    override fun iterator(): MutableIterator<E> = object : MutableIterator<E> {
        val wraps = inner.iterator()
        var lastElement: E? = null
        override fun hasNext(): Boolean = wraps.hasNext()
        override fun next(): E = wraps.next().also { lastElement = it }
        override fun remove() {
            wraps.remove()
            onRemove.runAll(lastElement as E)
        }
    }

    override fun remove(element: E): Boolean = inner.remove(element).also {
        if (it) onRemove.runAll(element)
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        var fail = false
        for (element in elements) {
            if (!remove(element)) fail = true
        }
        return fail
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        for (e in elements) {
            if (e !in elements) {
                onRemove.runAll(e)
            }
        }
        return inner.retainAll(elements)
    }
}