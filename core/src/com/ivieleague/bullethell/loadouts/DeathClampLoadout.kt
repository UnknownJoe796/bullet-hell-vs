package com.ivieleague.bullethell.loadouts

import com.badlogic.gdx.math.Vector2
import com.ivieleague.bullethell.GunLoadout
import com.ivieleague.bullethell.ShipInterface
import com.ivieleague.kotlin.Vector2_polar

/**
 * Created by joseph on 4/2/17.
 */
class DeathClampLoadout : GunLoadout {

    var baseAngle = 0f
    var swing = 0.0

    val bulletSize = .5f

    override fun doButton(shipInterface: ShipInterface, button: Int) {
        when (button) {
            0 -> {
                val main = Vector2(2f, 0f)
                val acc = Vector2(0f, 0f)
                for (i in 1..7) {
                    main.x += 1f
                    main.y = 20f
                    acc.y = -20f
                    shipInterface.shoot(
                            velocity = main.copy().rotateRad(baseAngle),
                            acceleration = acc.copy().rotateRad(baseAngle),
                            size = bulletSize
                    )
                    main.x += 1f
                    main.y = -20f
                    acc.y = 20f
                    shipInterface.shoot(
                            velocity = main.copy().rotateRad(baseAngle),
                            acceleration = acc.copy().rotateRad(baseAngle),
                            size = bulletSize
                    )
                }
            }
            1 -> {
                val density = 5
                for (angleInt in -density..density) {
                    shipInterface.shoot(
                            velocity = Vector2_polar(
                                    20f,
                                    (angleInt / density.toDouble()) * Math.PI * .5
                            ).apply {
                                x += 4f + Math.random().toFloat()
                                rotateRad(baseAngle)
                            },
                            acceleration = Vector2_polar(
                                    -15f,
                                    (angleInt / density.toDouble()) * Math.PI * .5
                            ).apply {
                                x += 4f + Math.random().toFloat()
                                rotateRad(baseAngle)
                            },
                            size = bulletSize
                    )
                }
            }
            2 -> {
                val density = 30
                for (angleInt in -density..density) {
                    shipInterface.shoot(
                            velocity = Vector2_polar(
                                    4f,
                                    (angleInt / density.toDouble()) * Math.PI * 2 + baseAngle
                            ),
                            acceleration = Vector2_polar(
                                    -1f,
                                    (angleInt / density.toDouble()) * Math.PI * 2 + baseAngle
                            ),
                            size = bulletSize
                    )
                }
            }
            3 -> {
                repeat(25) {
                    shipInterface.shoot(Vector2_polar(48f, baseAngle.toDouble()), Vector2.Zero, 2f)
                }
            }
            4 -> shipInterface.boost(.3f)
            5 -> {
                repeat(25) {
                    shipInterface.shoot(Vector2_polar(24f, baseAngle.toDouble()), Vector2.Zero, 1f)
                }
            }
            6 -> {
            }
            7 -> baseAngle += Math.PI.toFloat()
            8 -> baseAngle += Math.PI.toFloat() * .25f
            9 -> baseAngle -= Math.PI.toFloat() * .25f
        }
    }

    fun Vector2.copy() = Vector2(this)
}