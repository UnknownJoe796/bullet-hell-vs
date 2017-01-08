package com.ivieleague.lifecycle

import com.lightningkite.kotlin.lifecycle.LifecycleConnectable
import com.lightningkite.kotlin.lifecycle.LifecycleListener

/**
 * Created by josep on 1/7/2017.
 */
inline fun LifecycleConnectable.connect(crossinline onStart: () -> Unit, crossinline onStop: () -> Unit) {
    connect(object : LifecycleListener {
        override fun onStart() = onStart()
        override fun onStop() = onStop()
    })
}