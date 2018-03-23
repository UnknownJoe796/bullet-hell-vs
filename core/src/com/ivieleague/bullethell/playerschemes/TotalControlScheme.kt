package com.ivieleague.bullethell.playerschemes

import com.ivieleague.bullethell.entities.Player
import com.ivieleague.bullethell.entities.PlayerController
import com.ivieleague.bullethell.entities.PlayerInterface
import com.ivieleague.kotlin.Vector2_polar
import com.ivieleague.kotlin.minus
import lk.kotlin.jvm.utils.random.random

class TotalControlScheme : PlayerController {

    var baseAim = 0f
    fun defaultShotVel() = Vector2_polar(10f, baseAim + (-0.25..0.25).random())

    override fun invoke(ship: PlayerInterface, time: Float) = with(ship) {
        val speed = if (controls.buttons[4])
            Player.MAX_FREE_MOVEMENT * 2
        else
            Player.MAX_FREE_MOVEMENT

        move(controls.joystick.cpy().clamp(0f, 1f).scl(speed)).invoke()

        if (controls.buttonJustPressed(0)) {
            fire(defaultShotVel()).invoke()
        }
        if (controls.buttonJustPressed(1)) {
            fire(defaultShotVel(), energy = .15f, controller = {
                if (controls.buttons[5]) {
                    val towardsMe = ship.positionImmutable.cpy().minus(positionImmutable.cpy()).clamp(0f, 1f).scl(40f)
                    accelerateAbsolute(towardsMe).invoke()
                }
            }).invoke()
        }
        if (controls.buttonJustPressed(2)) {
//            fire(
//                    velocity = Vector2(5f, 0f),
//                    energy = .9f,
//                    size = .5f
//            ){
//                if (controls.buttonJustPressed(6)) {
//                    fire(
//                            velocity =
//                    )
//                }
//            }.invoke()
        }

        //Turning
        if (controls.buttonJustPressed(8)) {
            baseAim += (Math.PI / 4).toFloat()
        }
        if (controls.buttonJustPressed(9)) {
            baseAim -= (Math.PI / 4).toFloat()
        }
    }
}