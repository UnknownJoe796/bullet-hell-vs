package com.ivieleague.bullethell

import com.badlogic.gdx.math.Vector2
import com.ivieleague.kotlin.Vector2_polar
import java.lang.Math.sin

/**
 * Created by Brady on 4/2/2017.
 */
class BradysGunLoadout:GunLoadout {

    var secondSet = true
    var sweetheartMod = 1
    var swingerMod = .0f
    var swingerMod2 = true
    var reverseMod = 1f

    override fun doButton(shipInterface: ShipInterface, button: Int) {
        if(button == 8){
            secondSet = !secondSet
            println(secondSet)
            return
        }
        if (button == 7)
            reverseMod *= -1
        if(secondSet) {


            when (button) {
                0 -> {
                    shipInterface.shoot(Vector2(25f * reverseMod, sweetheartMod * 13f * reverseMod), Vector2(-10f * reverseMod, sweetheartMod * -8f * reverseMod), .5f)
                    sweetheartMod *= -1
                }
                1 -> {
                    shipInterface.shoot(Vector2(20f * reverseMod, swingerMod * reverseMod) , Vector2.Zero, .7f)

                    if(swingerMod2)
                        swingerMod += 3
                    else
                        swingerMod -= 3
                    if (swingerMod == -15f || swingerMod == 15f)
                         swingerMod2= !swingerMod2

                }
                2 -> {
                    for (horiz in -3..3) {
                        shipInterface.shoot(Vector2_polar(12f * reverseMod, horiz * Math.PI * .125f ), Vector2(0f, horiz * Math.PI.toFloat() * .125f * -5 * reverseMod), .5f)

                    }
                }
                3 -> {
                    shipInterface.shoot(Vector2(10f, swingerMod * .5f) , Vector2.Zero, 1.75f)
                    if(swingerMod2)
                        swingerMod += 3
                    else
                        swingerMod -= 3
                    if (swingerMod == -15f || swingerMod == 15f)
                        swingerMod2= !swingerMod2
                }

                4 -> {
                    shipInterface.boost(.3f)
                }
                5 -> {
                    shipInterface.shoot(Vector2(32f , 0f), Vector2(-24f, 0f), .5f)
                }
                6 -> {
                    shipInterface.shoot(Vector2(24f, 24f), Vector2(0f, -24f), .5f)
                }


            }
        }else{

            when (button) {
                0 -> {
                    for (horiz in -1..1) {

                        shipInterface.shoot(Vector2_polar(20f * reverseMod, horiz * Math.PI * .125f), Vector2(0f, swingerMod * .5f * reverseMod), .4f)
                        if (swingerMod2)
                            swingerMod += 3
                        else
                            swingerMod -= 3
                        if (swingerMod == -15f || swingerMod == 15f)
                            swingerMod2 = !swingerMod2
                    }
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