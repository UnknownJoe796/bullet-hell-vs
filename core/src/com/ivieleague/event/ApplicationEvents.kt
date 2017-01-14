package com.ivieleague.event

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.lightningkite.kotlin.runAll
import java.util.*

/**
 * Created by josep on 1/10/2017.
 */
abstract class ApplicationEvents() : ApplicationListener {

    val onPause = TreeSet<PriorityListener0>()
    override fun pause() {
        onPause.runAll()
    }

    val onResume = TreeSet<PriorityListener0>()
    override fun resume() {
        onPause.runAll()
    }

    val onResize = TreeSet<PriorityListener2<Int, Int>>()
    override fun resize(width: Int, height: Int) = onResize.runAll(width, height)

    val onRender = TreeSet<PriorityListener1<Float>>()
    override fun render() = onRender.runAll(Gdx.graphics.deltaTime)
}