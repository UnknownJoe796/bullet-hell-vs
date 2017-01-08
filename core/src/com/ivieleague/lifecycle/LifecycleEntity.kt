package com.ivieleague.lifecycle

import com.lightningkite.kotlin.lifecycle.LifecycleConnectable
import com.lightningkite.kotlin.lifecycle.LifecycleListener
import java.util.*

/**
 * Created by josep on 1/7/2017.
 */
open class LifecycleEntity : LifecycleConnectable, LifecycleListener {


    var canAdd = true
    var isGoing = false
    val listeners = ArrayList<LifecycleListener>()

    override fun connect(listener: LifecycleListener) {
        if (!canAdd) throw IllegalStateException()
        if (isGoing) {
            listener.onStart()
        }
        listeners.add(listener)
    }

    override fun onStart() {
        canAdd = false
        listeners.forEach { it.onStart() }
    }

    override fun onStop() {
        listeners.forEach { it.onStop() }
    }
}