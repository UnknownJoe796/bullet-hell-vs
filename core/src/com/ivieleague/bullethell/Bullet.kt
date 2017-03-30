package com.ivieleague.bullethell

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.ivieleague.kotlin.plusAssign
import com.ivieleague.kotlin.times
import com.ivieleague.rendering.InMemoryMesh
import com.ivieleague.service.getType

class Bullet : Entity {
    override val depth: Int get() = 1

    val position = Vector2()
    val velocity = Vector2()
    var color = Color.RED
    var radius = .5f
    var damage = .1f
    var life = 7f


    override fun step(world: World, time: Float) {
        position += velocity * time
        life -= time
        if (life <= 0f) world.entities -= this
    }

    companion object {
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
        renderer.batchingRenderer.append(transformedMesh)
    }
}
