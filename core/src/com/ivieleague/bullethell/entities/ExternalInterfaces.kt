package com.ivieleague.bullethell.entities

import com.badlogic.gdx.math.Vector2
import com.ivieleague.kotlin.Vector2Immutable

interface PlayerInterface {
    val energy: Float
    val health: Float
    val positionImmutable: Vector2Immutable

    val controls: Controls

    fun move(
            amount: Vector2
    ): PotentialAction

    fun fire(
            velocity: Vector2,
            size: Float = 1f,
            energy: Float = 0f,
            controller: BulletInterface.(Float) -> Unit = {}
    ): PotentialAction
}

interface BulletInterface {
    val energy: Float
    val size: Float
    val positionImmutable: Vector2Immutable
    val velocityImmutable: Vector2Immutable

    fun accelerate(
            amount: Vector2
    ): PotentialAction

    fun fire(
            velocity: Vector2,
            size: Float = 1f,
            energy: Float = 0f,
            controller: BulletInterface.(Float) -> Unit = {}
    ): PotentialAction
}

interface PotentialAction {
    val cost: Float
    fun invoke()
}

