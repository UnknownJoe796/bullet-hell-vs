package com.ivieleague.bullethell.playerschemes

import com.badlogic.gdx.math.Vector2
import com.ivieleague.bullethell.entities.Player
import com.ivieleague.bullethell.entities.PlayerController
import com.ivieleague.bullethell.entities.PlayerInterface
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
            fire(Vector2(10f, -5f), energy = .2f, size = .5f , controller = {
                if(velocityImmutable.x > 0){
                    accelerateRelative(Vector2(0f, 1f)).invoke()

                }
                if (controls.buttonJustPressed(1)) {
                    fire(Vector2(-1f, 5f), size = .5f).invoke()
                }
            }).invoke()

            fire(Vector2(10f, 5f), energy = .2f, size = .5f, controller = {
                if(velocityImmutable.x < 0){
                    accelerateRelative(Vector2(0f, -1f)).invoke()

                }
                if (controls.buttonJustPressed(1) ){
                    fire(Vector2(-1f, -5f), size = .5f).invoke()
                }
            }).invoke()
        }

        //button 3
        if (controls.buttonJustPressed(2)) {
            var iterator = 0.0
            fire(Vector2(15f, -5f), energy = .15f, size = .5f, controller = { secondsPassed:Float ->

                var curve = 25 * cos(iterator)

                accelerateRelative(Vector2(0f, curve.toFloat())).invoke()
                iterator += secondsPassed * PI/2
            }).invoke()
            fire(Vector2(15f, 5f), energy = .15f, size = .5f, controller = { secondsPassed:Float ->

                var curve = 25 * cos(iterator)

                accelerateRelative(Vector2(0f, curve.toFloat())).invoke()
                iterator += secondsPassed * PI/2
            }).invoke()
        }
        if (controls.buttonJustPressed(3)) {
            fire(Vector2(0f, 0f), energy = .25f).invoke()
        }

    }
}