package com.ivieleague.bullethell.playerschemes

import com.badlogic.gdx.math.Vector2
import com.ivieleague.bullethell.entities.Bullet
import com.ivieleague.bullethell.entities.Player
import com.ivieleague.bullethell.entities.PlayerController
import com.ivieleague.bullethell.entities.PlayerInterface
import com.ivieleague.bullethell.lib.sqr
import com.ivieleague.kotlin.Vector2_polar
import com.ivieleague.kotlin.addPolar
import com.ivieleague.kotlin.minus
import lk.kotlin.jvm.utils.random.random
import lk.kotlin.utils.math.toRadians

class TotalControlScheme : PlayerController {

    var baseAim = 0f
    fun defaultShotVel() = Vector2_polar(10f, baseAim + (-0.25..0.25).random())

    override fun invoke(ship: PlayerInterface, time: Float) {
        val speed = if (ship.controls.buttons[4])
            Player.MAX_FREE_MOVEMENT * 2
        else
            Player.MAX_FREE_MOVEMENT

        ship.move(ship.controls.joystick.cpy().clamp(0f, 1f).scl(speed)).invoke()

        if (ship.controls.buttonJustPressed(0)) {
            shotgun(ship)
        }
        if (ship.controls.buttonJustPressed(1)) {
            collapser(ship, defaultShotVel(), { ship.controls.buttons[5] })
        }
        if (ship.controls.buttonJustPressed(2)) {
            backsploder(ship, { ship.controls.buttonJustPressed(6) })
        }
        if (ship.controls.buttonJustPressed(3)) {
            sniper(ship)
        }
        if (ship.controls.buttonJustPressed(7)) {
            wallShot(ship)
        }

        //Turning
        if (ship.controls.buttons[8]) {
            baseAim += time * 2
        }
        if (ship.controls.buttons[9]) {
            baseAim -= time * 2
        }
    }

    private fun wanderer(ship: PlayerInterface) {
        val drag = Vector2_polar(4f, Math.random() * Math.PI * 2)
        ship.fire(Vector2_polar(15f, baseAim.toDouble()), energy = .05f) {
            accelerateAbsolute(drag).invoke()
        }.invoke()
    }

    private fun shotgun(ship: PlayerInterface) {
        val count = 7
        val startAngle = (-25).toFloat().toRadians()
        val endAngle = (25).toFloat().toRadians()
        val calls = (0 until count).map {
            val angle = (endAngle - startAngle) * (it.toFloat() / count.minus(1)) + startAngle
            ship.fire(Vector2_polar(15f, baseAim + angle.toDouble()))
        }
        if (calls.sumByDouble { it.cost.toDouble() }.toFloat() < ship.energy) {
            calls.forEach { it.invoke() }
        }
    }

    private fun sniper(ship: PlayerInterface) {
        ship.fire(Vector2_polar(40f, baseAim.toDouble())).invoke()
    }

    private fun wallShot(ship: PlayerInterface) {
        fun calcAccelCost(time: Float, startVel: Float): Float {
            val length = startVel / time
            val secondCost = length * Bullet.ACCELERATE_COST + length.sqr() * Bullet.ACCELERATE_SQUARED_COST
            return secondCost * time
        }

        fun fireSide(time: Float, startVel: Float) {
            ship.fire(Vector2(10f, startVel), size = 1f, energy = calcAccelCost(time, Math.abs(startVel))) {
                accelerateRelative(Vector2(0f, -startVel / time)).invoke()
            }.invoke()
        }
        for (amount in -10..10 step 2) {
            fireSide(2f, amount.toFloat())
        }
    }

    private fun backsploder(ship: PlayerInterface, activateCondition: () -> Boolean) {
        val count = 7
        val explodeCost = ship.fire(velocity = Vector2(0f, 10f)).cost * count + .001f
        ship.fire(
                velocity = Vector2(8f, 0f),
                energy = explodeCost,
                size = .5f
        ) {
            if (activateCondition()) {
                val startAngle = (180 - 45).toFloat().toRadians()
                val endAngle = (180 + 45).toFloat().toRadians()
                repeat(count) {
                    val angle = (endAngle - startAngle) * (it.toFloat() / count) + startAngle
                    fire(Vector2_polar(10f, angle.toDouble())).invoke()
                }
            }
        }.invoke()
    }

    private fun exploder(ship: PlayerInterface, activateCondition: () -> Boolean) {
        val sevenBulletCost = ship.fire(velocity = Vector2(0f, 5f)).cost * 7 + .001f
        ship.fire(
                velocity = Vector2(5f, 0f),
                energy = sevenBulletCost,
                size = .5f
        ) {
            if (activateCondition()) {
                repeat(7) {
                    fire(Vector2_polar(5f, Math.PI * 2 * (it.toDouble() / 7))).invoke()
                }
            }
        }.invoke()
    }

    private fun yankBack(ship: PlayerInterface, initVel: Vector2, pullCondition: () -> Boolean) {
        ship.fire(initVel, energy = .15f, controller = {
            if (pullCondition()) {
                val towardsMe = ship.positionImmutable.cpy().minus(positionImmutable.cpy()).clamp(0f, 1f).scl(40f)
                accelerateAbsolute(towardsMe).invoke()
            }
        }).invoke()
    }

    private fun collapser(ship: PlayerInterface, initVel: Vector2, pullCondition: () -> Boolean) {
        ship.fire(initVel, energy = .15f, controller = {
            if (pullCondition()) {
                val target = positionImmutable.cpy().addPolar(20f, baseAim.toDouble() - ship.angle)
                val towardsTarget = ship.positionImmutable.cpy().minus(target).clamp(0f, 1f).scl(40f)
                accelerateAbsolute(towardsTarget).invoke()
            }
        }).invoke()
    }
}