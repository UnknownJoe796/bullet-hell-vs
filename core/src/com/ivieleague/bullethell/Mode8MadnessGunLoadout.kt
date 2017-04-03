package com.ivieleague.bullethell

import com.badlogic.gdx.math.Vector2
import com.ivieleague.kotlin.Vector2_polar

/**
 * Created by joseph on 4/2/17.
 */
class Mode8MadnessGunLoadout : GunLoadout {

    var baseAngle = 0.0
    var swing = 0.0
    var rando = Math.PI * .05

    var randomMode = false
    var bigMode = false
    var fastMode = false

    override fun doButton(shipInterface: ShipInterface, button: Int) {
        when (button) {
            0 -> shipInterface.shoot(Vector2_polar(bulletSpeed, baseAngle.applyRando()), Vector2(-(bulletSpeed / 9f).sqr(), 0f), bulletSize)
            1 -> {
                swing += .2
                shipInterface.shoot(Vector2_polar(bulletSpeed, baseAngle.applyRando() + Math.sin(swing) * .5f), Vector2.Zero, bulletSize)
                shipInterface.shoot(Vector2_polar(bulletSpeed, baseAngle.applyRando() - Math.sin(swing) * .5f), Vector2.Zero, bulletSize)
            }
            2 -> {
                for (i in -10..10) {
                    shipInterface.shoot(Vector2_polar(bulletSpeed, baseAngle.applyRando() + i / 40.0 * Math.PI), Vector2.Zero, bulletSize)
                }
            }
            3 -> {
                swing += .2
                shipInterface.shoot(Vector2_polar(bulletSpeed, baseAngle.applyRando() + Math.sin(swing) * .5f), Vector2_polar(bulletSpeed, baseAngle.applyRando() - Math.sin(swing) * .5f).apply { x = 0f }, bulletSize)
                shipInterface.shoot(Vector2_polar(bulletSpeed, baseAngle.applyRando() - Math.sin(swing) * .5f), Vector2_polar(bulletSpeed, baseAngle.applyRando() + Math.sin(swing) * .5f).apply { x = 0f }, bulletSize)
            }
            4 -> shipInterface.boost(.3f)
            5 -> fastMode = !fastMode
            6 -> bigMode = !bigMode
            7 -> randomMode = !randomMode
            8 -> baseAngle += Math.PI * .25
            9 -> baseAngle -= Math.PI * .25
        }
    }

    val bulletSize get() = if (bigMode) 2f else .5f
    val bulletSpeed get() = if (fastMode) 24f else 12f
    fun Double.applyRando() = if (randomMode) this + (Math.random() * 2.0 - 1.0) * rando else this
}