package com.ivieleague.bullethell

import com.badlogic.gdx.math.Vector2
import com.ivieleague.kotlin.Vector2_polar

/**
 * Created by Brady on 4/2/2017.
 */
class BradysGunLoadout : GunLoadout {

    var secondSet = true
    var direction1 = 1
    var direction2 = .0f
    var direction2changer = true
    override fun doButton(shipInterface: ShipInterface, button: Int) {
        if (button == 8) {
            secondSet = !secondSet
            println(secondSet)
            return
        }
        if (secondSet) {


            when (button) {
                0 -> {
                    shipInterface.shoot(Vector2(20f, direction1 * 12f), Vector2(-10f, direction1 * -8f), .5f)
                    direction1 *= -1
                }
                1 -> {
                    shipInterface.shoot(Vector2(12f, direction2), Vector2.Zero, .7f)

                    if (direction2changer)
                        direction2 += 3
                    else
                        direction2 -= 3
                    if (direction2 == -12f || direction2 == 12f)
                        direction2changer = !direction2changer


                }
                2 -> {
                    for (horiz in -3..3) {
                        shipInterface.shoot(Vector2_polar(12f, horiz * Math.PI * .125f), Vector2(0f, horiz * Math.PI.toFloat() * .125f * -5), .5f)

                    }
                }
                3 -> {
                    for (horiz in -3..3) {
                        shipInterface.shoot(Vector2_polar(-12f, horiz * Math.PI * .125f), Vector2(0f, horiz * Math.PI.toFloat() * .125f * 5), .5f)
                    }
                }
                4 -> {
                    shipInterface.boost(.3f)
                }
                5 -> {
                    shipInterface.shoot(Vector2(32f, 0f), Vector2(-24f, 0f), .5f)
                }
                6 -> {
                    shipInterface.shoot(Vector2(24f, 24f), Vector2(0f, -24f), .5f)
                }


            }
        } else {

            when (button) {
                0 -> {
                    shipInterface.shoot(Vector2(12f, 0f), Vector2.Zero, 1f)
                }
                1 -> {
                    for (horiz in -1..1) {
                        shipInterface.shoot(Vector2_polar(12f, horiz * Math.PI * .125f), Vector2.Zero, 1f)
                    }
                }
                2 -> {
                    for (horiz in -5..5) {
                        shipInterface.shoot(Vector2_polar(12f, horiz * Math.PI * .125f), Vector2.Zero, 1f)
                    }
                }
                3 -> {
                    shipInterface.shoot(Vector2_polar(20f, 0.0), Vector2.Zero, 1f)
                }
                4 -> {
                    shipInterface.boost(.3f)
                }
                5 -> {
                    shipInterface.shoot(Vector2(32f, 0f), Vector2(-24f, 0f), 1f)
                }
                6 -> {
                    shipInterface.shoot(Vector2(24f, 24f), Vector2(0f, -24f), 1f)
                }


            }
        }
    }
}