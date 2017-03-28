package com.ivieleague.bullethellvs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.ivieleague.disposable.makeDisposable
import com.ivieleague.event.listen
import com.ivieleague.kotlin.aside
import com.ivieleague.kotlin.minus
import com.ivieleague.kotlin.toVector3
import com.ivieleague.physics.Box2DPhysics
import com.ivieleague.physics.addFixture
import com.ivieleague.physics.makeBody
import com.ivieleague.rendering.BatchingRenderer
import com.ivieleague.rendering.InMemoryMesh
import com.lightningkite.kotlin.Disposable

/**
 * Created by josep on 1/17/2017.
 */
class Player : GameModel<GameController, GameView> {

    val position = Vector2()
    val velocity = Vector2()
    val footVelocity = Vector2()
    val aim = Vector2(1f, 0f)
    var chargeTime = 0f

    companion object {
        const val ACCELERATION = 200f
        const val FRICTION = 200f
        const val MAX_FOOT_SPEED = 10f
        const val KNOCKBACK_SPEED = 35f
        const val CHARGE_TIME = .25f * .95f
    }

    override fun generateController(dependency: GameController): Disposable = makeDisposable {

        val physics = dependency.services.get<Box2DPhysics>().aside { add(it) }.service
        val module = physics.body(this@Player, null) {
            makeBody {
                type = BodyDef.BodyType.DynamicBody
                fixedRotation = true
                position.set(this@Player.position)
                velocity.set(this@Player.velocity)
            }.addFixture(CircleShape().apply { radius = .5f }) {
                friction = .3f
                density = 1f
            }
        }
//        FrictionJointDef().apply {
//            bodyA = module.body
//            bodyB = physics.staticBody
//            maxForce = FRICTION
//        }.let { physics.world.createJoint(it) }

        val joystick = Vector2()
        listen(dependency.step, -1) { time: Float ->
            joystick.setZero()
            if (Gdx.input.isKeyPressed(Input.Keys.A))
                joystick.x -= 1
            if (Gdx.input.isKeyPressed(Input.Keys.D))
                joystick.x += 1
            if (Gdx.input.isKeyPressed(Input.Keys.S))
                joystick.y -= 1
            if (Gdx.input.isKeyPressed(Input.Keys.W))
                joystick.y += 1
            if (joystick.len() > 1f) joystick.setLength(1f)
            if (joystick.len() > 0f) aim.set(joystick).setLength(1f)

            footVelocity.add(joystick.cpy().scl(time * ACCELERATION * 2))
            if (footVelocity.len() < time * ACCELERATION) {
                footVelocity.set(Vector2.Zero)
            } else {
                footVelocity.add(footVelocity.cpy().scl(-1f).setLength(time * ACCELERATION))
            }

            if (footVelocity.len() > MAX_FOOT_SPEED)
                footVelocity.setLength(MAX_FOOT_SPEED)

            //friction towards foot speed
            val dif = footVelocity - velocity
            if (dif.len() > time * FRICTION) dif.setLength(time * FRICTION)
            module.body.applyLinearImpulse(dif, Vector2.Zero, true)

            if (chargeTime > CHARGE_TIME && !Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                module.body.applyLinearImpulse(aim.cpy().setLength(KNOCKBACK_SPEED), Vector2.Zero, true)
                chargeTime = 0f
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
                chargeTime += time
            else
                chargeTime = 0f
        }
        listen(dependency.step, 1) {
            //save current
            position.set(module.body.position)
            velocity.set(module.body.linearVelocity)
        }
    }

    override fun generateView(dependency: GameView): Disposable = makeDisposable {
        val batchingRenderer = dependency.services.get<BatchingRenderer>().aside { add(it) }.service

        val mesh = InMemoryMesh.circle(Vector2.Zero, 0f, .5f, Color.BLUE, 5) + InMemoryMesh.circle(Vector2(.4f, 0f), 0f, .1f, Color.GOLD)
        val transformedMesh = mesh.duplicate()

        val chargeBar = InMemoryMesh.rectangle(Vector2(0f, .6f), 1f, .2f, color = Color.GOLD)
        val transformedChargeBar = chargeBar.duplicate()

        listen(dependency.render, 0) { time: Float ->
            val rotation = aim.angleRad()
            val matrix = Matrix4().apply {
                translate(position.toVector3())
                rotate(Vector3.Z, (rotation * 180 / Math.PI).toFloat())
            }
            transformedMesh.set(mesh)
            transformedMesh.transformAssign(matrix)
            batchingRenderer.append(transformedMesh)


            val barmatrix = Matrix4().apply {
                translate(position.toVector3())
                scale((chargeTime / CHARGE_TIME).coerceIn(0f, 1f), 1f, 1f)
            }
            transformedChargeBar.set(chargeBar)
            transformedChargeBar.transformAssign(barmatrix)
            batchingRenderer.append(transformedChargeBar)

            batchingRenderer.flush()

//            transformedMesh.set(mesh)
//            transformedMesh.translateAssign(position.toVector3())
//            batchingRenderer.append(transformedMesh)
//            batchingRenderer.flush()
        }

    }

}