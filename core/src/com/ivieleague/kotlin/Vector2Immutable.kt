package com.ivieleague.kotlin

import com.badlogic.gdx.math.Vector2

class Vector2Immutable(private val wraps: Vector2) {
    fun cpy(): Vector2 = wraps.cpy()
    val x get() = wraps.x
    val y get() = wraps.y
}

fun Vector2.immutable(): Vector2Immutable = Vector2Immutable(this)