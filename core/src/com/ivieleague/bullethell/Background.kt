package com.ivieleague.bullethell

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20

class Background(val color: Color = Color.BLACK) : Entity {
    override val depth: Int
        get() = -1000

    override fun step(world: World, time: Float) {}

    override fun draw(view: WorldView) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1f)
        Gdx.gl.glClear(
                GL20.GL_COLOR_BUFFER_BIT
                        or GL20.GL_DEPTH_BUFFER_BIT
                        or if (Gdx.graphics.bufferFormat.coverageSampling)
                    GL20.GL_COVERAGE_BUFFER_BIT_NV
                else
                    0
        )
    }
}