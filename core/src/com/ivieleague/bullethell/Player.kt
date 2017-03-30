package com.ivieleague.bullethell

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.ivieleague.kotlin.Vector2_polar
import com.ivieleague.kotlin.minus
import com.ivieleague.kotlin.plusAssign
import com.ivieleague.kotlin.times
import com.ivieleague.rendering.InMemoryMesh
import com.ivieleague.service.getType

class Player : Entity {
    override val depth: Int get() = 0

    val position = Vector2()
    val velocity = Vector2()
    var color = Color.RED
    var radius = .1f
    var angle = 0f

    var health = 1f
    var temp = 0f
    var burnout = false
    var boost = 0f

    companion object {
        const val cooldown: Float = 1f
    }

    fun reset() {
        health = 1f
        temp = 0f
        boost = 0f
        burnout = false
    }

    fun move(joy: Vector2) {
        if (boost > 0f) {
            velocity.set(joy.clamp(0f, 1f) * 20f)
        } else {
            velocity.set(joy.clamp(0f, 1f) * 10f)
        }
    }

    fun shoot(world: World, pattern: Int = 0) {
        if (burnout) return
        when (pattern) {
            0 -> {
                temp += .1f
                world.entities += Bullet().also { it ->
                    it.color = color
                    it.position.set(position)
                    it.velocity.set(Vector2_polar(12f, angle.toDouble()))
                }
            }
            1 -> {
                temp += .2f
                for (horiz in -1..1) {
                    world.entities += Bullet().also {
                        it.color = color
                        it.position.set(position)
                        it.velocity.set(Vector2_polar(12f, angle.toDouble() + horiz * Math.PI * .125f))
                    }
                }
            }
            2 -> {
                temp += .4f
                for (horiz in -5..5) {
                    world.entities += Bullet().also {
                        it.color = color
                        it.position.set(position)
                        it.velocity.set(Vector2_polar(12f, angle.toDouble() + horiz * Math.PI * .125f))
                    }
                }
            }
            3 -> {
                temp += .2f
                world.entities += Bullet().also {
                    it.color = color
                    it.position.set(position)
                    it.velocity.set(Vector2_polar(20f, angle.toDouble()))
                }
            }
            4 -> {
                temp += .4f
                boost = .3f
            }
        }
    }

    override fun step(world: World, time: Float) {
        position += velocity * time
        for (other in world.entities) {
            if (other is Bullet) {
                if (color != other.color && (other.position - position).len2() < (other.radius + radius).sqr()) {
                    //hit
                    health -= other.damage
                    world.entities -= other
                    world.events.dispatch(DamageEvent(this, other.damage))
                }
            }
        }
        boost -= time
        temp -= time * cooldown
        temp = temp.coerceAtLeast(0f)
        if (temp > 1f) {
            burnout = true
        } else if (temp == 0f) {
            burnout = false
        }
        if (health <= 0f) {
            world.entities -= this
            world.events.dispatch(DeathEvent(this))
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
            scale(temp * .5f, temp * .5f, 1f)
        })
        transformedCircle.transformColorAssign { colorFloatBits }
        renderer.batchingRenderer.append(transformedCircle)
    }
}