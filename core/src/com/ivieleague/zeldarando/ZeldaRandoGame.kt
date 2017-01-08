package com.ivieleague.zeldarando

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.ivieleague.event.listen
import com.ivieleague.lifecycle.LifecycleEntity
import com.ivieleague.lifecycle.MutableLifecyleEntity
import com.ivieleague.lifecycle.connect

class ZeldaRandoGame : EventApplicationListener() {

    lateinit var batch: SpriteBatch
    lateinit var img: Texture
    val entities = MutableLifecyleEntity().apply { this@ZeldaRandoGame.connect(this) }

    init {
        connect({ batch = SpriteBatch() }, { batch.dispose() })
        connect({ img = Texture("badlogic.jpg") }, { img.dispose() })

        //background
        connect(object : LifecycleEntity() {

            var time = 0.0

            init {
                listen(this@ZeldaRandoGame.onRender, 0) {
                    time += it
                    Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
                    batch.begin()
                    batch.draw(
                            img,
                            100f + 100f * Math.sin(time).toFloat(),
                            100f + 100f * Math.cos(time).toFloat()
                    )
                    batch.end()
                }
            }

        })

        //Controllable icon
        connect(object : LifecycleEntity() {

            val position = Vector2(0f, 0f)

            init {
                listen(this@ZeldaRandoGame.onRender, 1) { time ->
                    if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                        position.x -= time * 50f
                    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                        position.x += time * 50f
                    if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                        position.y -= time * 50f
                    if (Gdx.input.isKeyPressed(Input.Keys.UP))
                        position.y += time * 50f

                    batch.begin()
                    batch.draw(
                            img,
                            position.x,
                            position.y
                    )
                    batch.end()
                }
            }
        })
    }
}
