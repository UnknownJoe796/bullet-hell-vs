package com.ivieleague.bullethell

import com.badlogic.gdx.math.Vector2
import com.ivieleague.kotlin.Vector2_polar

/**
 * Created by Brady on 4/2/2017.
 */
class BradysGunLoadout:GunLoadout {

    var secondSet = true

    override fun doButton(shipInterface: ShipInterface, button: Int) {
        when (button) {
            0 -> {
                shipInterface.shoot(Vector2(12f, 0f), Vector2.Zero, .5f)
            }
            1 -> {
                for (horiz in -1..1) {
                    shipInterface.shoot(Vector2_polar(12f, horiz * Math.PI * .125f), Vector2.Zero, .5f)
                }
            }
            2 -> {
                for (horiz in -5..5) {
                    shipInterface.shoot(Vector2_polar(12f, horiz * Math.PI * .125f), Vector2.Zero, .5f)
                }
            }
            3 -> {
                shipInterface.shoot(Vector2_polar(20f, 0.0), Vector2.Zero, .5f)
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
    }
}