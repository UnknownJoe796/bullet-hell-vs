package com.ivieleague.zeldarando

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.ivieleague.event.PriorityListener1
import com.lightningkite.kotlin.lifecycle.LifecycleConnectable
import com.lightningkite.kotlin.lifecycle.LifecycleListener
import com.lightningkite.kotlin.runAll
import java.util.*

/**
 * Created by josep on 1/7/2017.
 */
abstract class EventApplicationListener : ApplicationListener, LifecycleConnectable {

    private val listeners = HashSet<LifecycleListener>()
    override fun connect(listener: LifecycleListener) {
        if (isCreated) {
            listener.onStart()
        }
        listeners.add(listener)
    }

    fun disconnect(listener: LifecycleListener) {
        listener.onStop()
        listeners.remove(listener)
    }

    var isCreated = false

    override fun create() {
        isCreated = true

        for (listener in listeners) {
            listener.onStart()
        }
    }

    override fun dispose() {
        isCreated = false

        for (listener in listeners) {
            listener.onStop()
        }
    }

    val onResize = HashSet<(Int, Int) -> Unit>()
    override fun resize(width: Int, height: Int) = onResize.runAll(width, height)

    val onRender = TreeSet<PriorityListener1<Float>>()
    override fun render() = onRender.runAll(Gdx.graphics.deltaTime)

    override fun pause() {}
    override fun resume() {}
}