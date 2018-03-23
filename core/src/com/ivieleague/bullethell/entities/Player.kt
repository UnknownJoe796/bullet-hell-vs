package com.ivieleague.bullethell.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.ivieleague.bullethell.lib.*
import com.ivieleague.kotlin.*
import com.ivieleague.rendering.InMemoryMesh
import com.ivieleague.service.getType

class Player(
        val controller: PlayerController,
        var color: Color = Color.RED,
        var angle: Float = 0f,
        var radius: Float = .1f,
        var energy: Float = MAX_ENERGY,
        var health: Float = MAX_HEALTH,
        val position: Vector2 = Vector2(),
        val controls: Controls = Controls()
) : Entity {
    override val depth: Int get() = 0

    fun reset() {
        health = MAX_HEALTH
        energy = MAX_ENERGY
    }

    companion object {
        const val MAX_HEALTH: Float = 1f
        const val MAX_ENERGY: Float = 1f
        const val RECHARGE: Float = 1f
        const val MAX_FREE_MOVEMENT: Float = 10f
        const val ADDITIONAL_MOVEMENT_COST_MULTIPLIER: Float = .25f

        const val SIZE_COST: Float = .1f
        const val VELOCITY_COST: Float = .0025f
    }

    private var lastDeltaTime: Float = 0f

    class MyInterface(val player: Player, var world: World, var seconds: Float) : PlayerInterface {
        override val energy: Float get() = player.energy
        override val health: Float get() = player.health
        override val positionImmutable: Vector2Immutable = player.position.immutable()
        override val controls: Controls get() = player.controls

        override fun move(amount: Vector2): PotentialAction = object : PotentialAction {
            override val cost: Float = amount.len().minus(MAX_FREE_MOVEMENT).times(seconds).times(ADDITIONAL_MOVEMENT_COST_MULTIPLIER).coerceAtLeast(0f)

            override fun invoke() {
                if (cost > player.energy) return
                player.energy -= cost

                player.position.add(amount.times(seconds).rotateRad(player.angle))
            }
        }

        override fun fire(
                velocity: Vector2,
                size: Float,
                energy: Float,
                controller: BulletInterface.(Float) -> Unit
        ): PotentialAction = object : PotentialAction {
            override val cost: Float = velocity.len().times(VELOCITY_COST) + size.times(SIZE_COST) + energy

            override fun invoke() {
                if (cost > player.energy) return
                player.energy -= cost

                world.entities += Bullet(player.angle).also { it ->
                    it.color = player.color
                    it.position.set(player.position)
                    it.velocity.set(velocity.rotateRad(player.angle))
                    it.energy = energy
                    it.controller = controller
                    it.radius = size
                }
            }
        }
    }

    override fun step(world: World, time: Float) {
        for (other in world.entities) {
            if (other is Bullet) {
                if ((other.position - position).len2() < (other.radius + radius).sqr()) {

                    if (color == other.color) {
                        if (other.activeFor > 1f) {
                            //absorb energy
                            energy += other.energy
                            world.entities -= other
                        }
                    } else {
                        //hit
                        health -= other.damage
                        world.entities -= other
                        world.events.dispatch(DamageEvent(this, other.damage))
                    }
                }
            }
        }
        energy = (energy + RECHARGE * time).coerceAtMost(MAX_ENERGY)

        controller.invoke(MyInterface(this, world, time), time)

        if (health <= 0f) {
            world.entities -= this
            world.events.dispatch(DeathEvent(this))
            for (angleInt in 0..32) {
                world.entities += Bullet(angle).also { it ->
                    it.color = color
                    it.position.set(position)
                    it.velocity.set(Vector2_polar(8f, angleInt / 32.0 * Math.PI * 2))
                }
            }
        }
    }

    class DamageEvent(val player: Player, val damage: Float)
    class DeathEvent(val player: Player)

    @Transient val mesh: InMemoryMesh = InMemoryMesh.polygon(
            listOf(
                    Vector2(0f, 1f),
                    Vector2(1f, -1f),
                    Vector2(0f, -.5f),
                    Vector2(-1f, -1f)
            ),
            0f,
            color
    )
    @Transient val transformedMesh = mesh.duplicate()

    @Transient val bar = InMemoryMesh.rectangle(Vector2(0f, 0f), 1f, .2f, color = Color.WHITE)
    @Transient val transformedBar = bar.duplicate()

    @Transient val circle = InMemoryMesh.circle(Vector2(0f, 0f), 0f, 1.1f, color = Color.WHITE)
    @Transient val transformedCircle = circle.duplicate()

    override fun draw(view: WorldView) {
        val colorFloatBits = color.toFloatBits()

        val renderer = view.services.getType { BatchingRendererService(view.matrix) }
        transformedMesh.set(mesh)
        transformedMesh.transformAssign(Matrix4().apply {
            translate(position.x, position.y, 0f)
            rotateRad(Vector3.Z, angle - Math.PI.toFloat() / 2f)
        })
        transformedMesh.transformColorAssign { colorFloatBits }
        renderer.batchingRenderer.append(transformedMesh)

        transformedCircle.set(circle)
        transformedCircle.transformAssign(Matrix4().apply {
            translate(
                    position.x - Math.cos(angle.toDouble()).toFloat(),
                    position.y - Math.sin(angle.toDouble()).toFloat(),
                    0f
            )
            scale(health * .5f, health * .5f, 1f)
        })
        transformedCircle.transformColorAssign { colorFloatBits }
        renderer.batchingRenderer.append(transformedCircle)

        transformedCircle.set(circle)
        transformedCircle.transformAssign(Matrix4().apply {
            translate(
                    position.x + Math.cos(angle.toDouble()).toFloat(),
                    position.y + Math.sin(angle.toDouble()).toFloat(),
                    0f
            )
            scale(energy * .5f / MAX_ENERGY, energy * .5f / MAX_ENERGY, 1f)
        })
        transformedCircle.transformColorAssign { colorFloatBits }
        renderer.batchingRenderer.append(transformedCircle)
    }
}