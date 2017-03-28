package com.ivieleague.kotlin

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

fun Vector2_polar(distance: Float, radians: Double): Vector2 {
    return Vector2(Math.cos(radians).toFloat() * distance, Math.sin(radians).toFloat() * distance)
}

fun Vector2_polarDegrees(distance: Float, degrees: Float): Vector2 {
    return Vector2(Math.cos(degrees * 180 / Math.PI).toFloat() * distance, Math.sin(degrees * 180 / Math.PI).toFloat() * distance)
}

fun Vector2.setPolar(distance: Float, radians: Double): Vector2 {
    x = Math.cos(radians).toFloat() * distance
    y = Math.sin(radians).toFloat() * distance
    return this
}

fun Vector2.addPolar(distance: Float, radians: Double): Vector2 {
    x += Math.cos(radians).toFloat() * distance
    y += Math.sin(radians).toFloat() * distance
    return this
}

fun Vector3.toVector2(): Vector2 {
    return Vector2(x, y)
}

fun Vector2.toVector3(): Vector3 {
    return Vector3(x, y, 0f)
}

operator fun Vector2.plus(other: Vector2): Vector2 = this.cpy().add(other)
operator fun Vector2.plusAssign(other: Vector2) { this.add(other) }
operator fun Vector2.minus(other: Vector2): Vector2 = this.cpy().sub(other)
operator fun Vector2.minusAssign(other: Vector2) { this.sub(other) }
operator fun Vector2.times(other: Vector2): Vector2 = this.cpy().scl(other)
operator fun Vector2.timesAssign(other: Vector2) { this.scl(other) }
operator fun Vector2.times(other: Float): Vector2 = this.cpy().scl(other)
operator fun Vector2.timesAssign(other: Float) { this.scl(other) }
operator fun Vector2.div(other: Float): Vector2 = this.cpy().scl(1 / other)
operator fun Vector2.divAssign(other: Float) { this.scl(1 / other) }