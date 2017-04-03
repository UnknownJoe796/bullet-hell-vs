package com.ivieleague.bullethell.loadouts

import com.badlogic.gdx.math.Vector2
import com.ivieleague.bullethell.GunLoadout
import com.ivieleague.bullethell.ShipInterface
import com.ivieleague.kotlin.Vector2_polar

/**
 * Created by Brady on 4/2/2017.
 */
class BradysGunLoadout : GunLoadout {

    var secondSet = true
    var sweetheartMod = 1
    var swingerMod = .0f
    var swingerMod2 = true
    var reverseMod = 1f
    var distanceMod = 1f

    override fun doButton(shipInterface: ShipInterface, button: Int) {
        if(button == 8){
            secondSet = !secondSet
            println(secondSet)
            return
        }
        if (button == 9)
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
                    shipInterface.shoot(Vector2(10f * reverseMod, swingerMod * .5f) , Vector2.Zero, 1.75f)
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
                7 -> {
                    shipInterface.shoot(Vector2(0f, 0f), Vector2(0f, 0f), 2f)
                }


            }
        }else{

            when (button) {
                0 -> {
                    for (horiz in -1..1) {

                        shipInterface.shoot(Vector2_polar(15f * reverseMod, horiz * Math.PI * .125f), Vector2(0f, swingerMod * .5f * reverseMod), .5f)
                        if (swingerMod2)
                            swingerMod += 3
                        else
                            swingerMod -= 3
                        if (swingerMod == -15f || swingerMod == 15f)
                            swingerMod2 = !swingerMod2
                    }
                }
                1 -> {
                    shipInterface.shoot(Vector2(30f * reverseMod, 0f), Vector2(-10f, 0f), .5f)
                }
                2 -> {
                    shipInterface.shoot(Vector2(10 * distanceMod * reverseMod, 0f), Vector2(0f, 5f), .5f)

                    distanceMod += .5f
                    if(distanceMod == 3f)
                        distanceMod = 1f

                }
                3 -> {
                    shipInterface.shoot(Vector2(10 * distanceMod * reverseMod, 0f), Vector2(0f, -5f), .5f)

                    distanceMod += .5f
                    if(distanceMod == 3f)
                        distanceMod = 1f
                }
                4 -> {
                    shipInterface.boost(.3f)
                }
                7 -> {
                    shipInterface.shoot(Vector2(0f, 0f), Vector2(0f, 0f), 2f)
                }
                6 -> {
                    shipInterface.shoot(Vector2(24f, 24f), Vector2(0f, -24f), 1f)
                }


            }
        }
    }
}