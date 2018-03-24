package com.ivieleague.bullethell.playerschemes

import com.badlogic.gdx.math.Vector2
import com.ivieleague.bullethell.entities.Player
import com.ivieleague.bullethell.entities.PlayerController
import com.ivieleague.bullethell.entities.PlayerInterface
import com.ivieleague.kotlin.Vector2_polar
import com.ivieleague.kotlin.minus
import java.lang.Math.*
import kotlin.math.PI

class BradysScheme : PlayerController {
    override fun invoke(ship: PlayerInterface, time: Float) = with(ship) {

        val speed = if (controls.buttons[8])
            Player.MAX_FREE_MOVEMENT * 2
        else
            Player.MAX_FREE_MOVEMENT

        move(controls.joystick.cpy().clamp(0f, 1f).scl(speed)).invoke()

        //Button 1
        if (controls.buttonJustPressed(0)) {
            fire(velocity = Vector2(10f, 0f), energy = .3f, size = .5f, controller = {
                if (velocityImmutable.x > 0) {
                    accelerateRelative(Vector2(0f, 1f)).invoke()

                }
                if (controls.buttonJustPressed(4)) {
                    fire(velocity = Vector2(0f, 10f), size = .5f).invoke()
                    fire(velocity = Vector2(0f, -10f), size = .5f).invoke()

                }
            }).invoke()


        }

        //button 3
        if (controls.buttonJustPressed(1)) {
            var iterator = 0.0
            fire(velocity = Vector2(10f, 0f), energy = .15f, size = .5f, controller = { secondsPassed: Float ->

                var curve = 10 * cos(iterator)

                accelerateRelative(Vector2(0f, curve.toFloat())).invoke()
                iterator += secondsPassed * PI / 2
            }).invoke()

        }

        //button 3
        if (controls.buttonJustPressed(2)) {
            fire(velocity = Vector2(10f, 0f), energy = .5f, size = .3f, controller = {

                if (controls.buttonJustPressed(6)) {
                    repeat(7) {
                        fire(velocity = Vector2_polar(5f, Math.PI * 2 * (it.toDouble() / 7)), size = .2f).invoke()
                    }

                }
            }).invoke()

        }
        if (controls.buttons[3]) {
            fire(velocity = Vector2(5f, 0f), size = .1f).invoke()

        }
    }
}