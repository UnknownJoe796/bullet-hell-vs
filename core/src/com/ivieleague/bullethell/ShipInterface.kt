package com.ivieleague.bullethell

import com.badlogic.gdx.math.Vector2

interface ShipInterface {
    fun shoot(velocity: Vector2, acceleration: Vector2, size: Float)
    fun boost(time: Float)
}