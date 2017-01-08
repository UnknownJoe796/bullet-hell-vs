package com.ivieleague.lifecycle

import com.lightningkite.kotlin.lifecycle.LifecycleConnectable
import com.lightningkite.kotlin.lifecycle.LifecycleListener
import java.util.*

/**
 * Created by josep on 1/7/2017.
 */
class MutableLifecyleEntity : MutableCollection<LifecycleListener>, LifecycleConnectable, LifecycleListener {

    var canAdd = true
    var isGoing = false
    val listeners = HashSet<LifecycleListener>()

    override fun connect(listener: LifecycleListener) {
        if (!canAdd) throw IllegalStateException()
        if (isGoing) {
            listener.onStart()
        }
        listeners.add(listener)
    }

    fun disconnect(listener: LifecycleListener) {
        listener.onStop()
        listeners.remove(listener)
    }

    override fun onStart() {
        canAdd = false
        isGoing = true
        HashSet(listeners).forEach { it.onStart() }
    }

    override fun onStop() {
        isGoing = false
        HashSet(listeners).forEach { it.onStop() }
    }

    override fun iterator(): MutableIterator<LifecycleListener> = listeners.toTypedArray().iterator().let {
        object : MutableIterator<LifecycleListener> {
            override fun hasNext(): Boolean = it.hasNext()
            var last: LifecycleListener? = null
            override fun next(): LifecycleListener {
                last = it.next()
                return last!!
            }

            override fun remove() {
                disconnect(last!!)
            }
        }
    }

    override val size: Int
        get() = listeners.size

    override fun contains(element: LifecycleListener): Boolean = listeners.contains(element)
    override fun containsAll(elements: Collection<LifecycleListener>): Boolean = listeners.containsAll(elements)
    override fun isEmpty(): Boolean = listeners.isEmpty()
    override fun add(element: LifecycleListener): Boolean {
        connect(element); return true
    }

    override fun addAll(elements: Collection<LifecycleListener>): Boolean {
        elements.forEach { connect(it) }; return true
    }

    override fun clear() {
        HashSet(listeners).forEach { it.onStop() }
    }

    override fun remove(element: LifecycleListener): Boolean {
        disconnect(element); return true
    }

    override fun removeAll(elements: Collection<LifecycleListener>): Boolean {
        elements.forEach { disconnect(it) }; return true
    }

    override fun retainAll(elements: Collection<LifecycleListener>): Boolean {
        listeners.filter { it in elements }.forEach { it.onStop() }
        return true
    }
}