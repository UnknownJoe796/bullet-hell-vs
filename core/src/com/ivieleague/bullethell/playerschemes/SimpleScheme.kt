package com.ivieleague.bullethell.playerschemes

import com.badlogic.gdx.math.Vector2
import com.ivieleague.bullethell.entities.Player
import com.ivieleague.bullethell.entities.PlayerController
import com.ivieleague.bullethell.entities.PlayerInterface
import com.ivieleague.kotlin.minus

class SimpleScheme : PlayerController {
    override fun invoke(ship: PlayerInterface, time: Float) = with(ship) {
        val speed = if (controls.buttons[8])
            Player.MAX_FREE_MOVEMENT * 2
        else
            Player.MAX_FREE_MOVEMENT

        move(controls.joystick.cpy().clamp(0f, 1f).scl(speed)).invoke()

        if (controls.buttonJustPressed(0)) {
            fire(Vector2(10f, 0f)).invoke()
        }
        if (controls.buttonJustPressed(1)) {
            fire(Vector2(10f, 0f), energy = .15f, controller = {
                if (controls.buttons[2]) {
                    val towardsMe = ship.positionImmutable.cpy().minus(positionImmutable.cpy()).clamp(0f, 1f).scl(40f)
                    accelerateAbsolute(towardsMe).invoke()
                }
            }).invoke()
        }
        if (controls.buttonJustPressed(3)) {
            fire(Vector2(0f, 0f), energy = .25f).invoke()
        }
    }
}