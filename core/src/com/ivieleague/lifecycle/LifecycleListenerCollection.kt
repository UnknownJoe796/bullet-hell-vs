package com.ivieleague.lifecycle

import com.lightningkite.kotlin.lifecycle.LifecycleListener

/**
 * Listener for start and stop events on a lifecycle.
 * Created by jivie on 6/1/16.
 */
interface LifecycleListenerCollection : LifecycleListener {
    val collection: Collection<LifecycleListener>
    override fun onStart() {
        collection.forEach { it.onStart() }
    }

    override fun onStop() {
        collection.forEach { it.onStop() }
    }

}

fun Collection<LifecycleListener>.merge(): LifecycleListenerCollection = object : LifecycleListenerCollection {
    override val collection: Collection<LifecycleListener> = this@merge
}