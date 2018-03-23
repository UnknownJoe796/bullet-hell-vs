package com.ivieleague.bullethell.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.ivieleague.bullethell.lib.*
import com.ivieleague.kotlin.*
import com.ivieleague.rendering.InMemoryMesh
import com.ivieleague.service.getType

class Bullet(
        val playerAngle: Float,
        var color: Color = Color.RED,
        var life: Float = 7f,
        var damage: Float = .1f,
        val position: Vector2 = Vector2(),
        val velocity: Vector2 = Vector2(),
        var radius: Float = .5f,
        var energy: Float = 0f,
        var controller: BulletInterface.(Float) -> Unit = {}
) : Entity {
    override val depth: Int get() = 1

    var activeFor: Float = 0f

    class MyInterface(val bullet: Bullet, var world: World, var seconds: Float) : BulletInterface {
        override val energy: Float get() = bullet.energy
        override val size: Float get() = bullet.radius
        override val positionImmutable: Vector2Immutable = bullet.position.immutable()
        override val velocityImmutable: Vector2Immutable = bullet.velocity.immutable()

        override fun accelerateAbsolute(amount: Vector2): PotentialAction = object : PotentialAction {
            override val cost = run {
                val length = amount.len()
                val secondCost = length * ACCELERATE_COST + length.sqr() * ACCELERATE_SQUARED_COST
                secondCost.times(seconds)
            }

            override fun invoke() {
                if (cost > bullet.energy) return
                bullet.energy -= cost

                bullet.velocity.add(amount.times(seconds))
            }
        }

        override fun accelerateRelative(amount: Vector2): PotentialAction = object : PotentialAction {
            override val cost = run {
                val length = amount.len()
                val secondCost = length * ACCELERATE_COST + length.sqr() * ACCELERATE_SQUARED_COST
                secondCost.times(seconds)
            }

            override fun invoke() {
                if (cost > bullet.energy) return
                bullet.energy -= cost

                bullet.velocity.add(amount.times(seconds).rotateRad(bullet.playerAngle))
            }
        }

        override fun fire(
                velocity: Vector2,
                size: Float,
                energy: Float,
                controller: BulletInterface.(Float) -> Unit
        ): PotentialAction = object : PotentialAction {
            override val cost: Float = velocity.len().times(Player.VELOCITY_COST) + size.times(Player.SIZE_COST) + energy

            override fun invoke() {
                if (cost > bullet.energy) return
                bullet.energy -= cost

                world.entities += Bullet(playerAngle = bullet.playerAngle).also { it ->
                    it.color = bullet.color
                    it.position.set(bullet.position)
                    it.velocity.set(bullet.velocity + velocity.rotateRad(bullet.playerAngle))
                    it.energy = energy
                    it.controller = controller
                    it.radius = size
                }
            }
        }
    }



    override fun step(world: World, time: Float) {
        position += velocity * time
        controller.invoke(MyInterface(this, world, time), time)
        activeFor += time
        life -= time
        if (life <= 0f) world.entities -= this
    }

    companion object {
        const val ACCELERATE_COST: Float = .0025f
        const val ACCELERATE_SQUARED_COST: Float = .000025f

        val mesh: InMemoryMesh = InMemoryMesh.circle(
                Vector2(0f, 0f),
                0f,
                1f,
                Color.WHITE
        )
        @Transient val transformedMesh = mesh.duplicate()
    }

    override fun draw(view: WorldView) {
        val renderer = view.services.getType { BatchingRendererService(view.matrix) }
        transformedMesh.set(mesh)
        transformedMesh.transformAssign(Matrix4().apply {
            translate(position.x, position.y, 0f)
            scale(radius, radius, 1f)
        })
        val colorbits = color.toFloatBits()
        transformedMesh.transformColorAssign { colorbits }
        renderer.batchingRenderer.append(transformedMesh)
    }
}
