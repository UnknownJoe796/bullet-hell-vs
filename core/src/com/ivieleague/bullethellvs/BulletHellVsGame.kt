package com.ivieleague.bullethellvs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.ivieleague.disposable.EmptyDisposable
import com.ivieleague.disposable.makeDisposable
import com.ivieleague.event.listen
import com.ivieleague.kotlin.aside
import com.ivieleague.rendering.BatchingRenderer
import com.lightningkite.kotlin.Disposable
import java.util.*

/**
 * Created by joseph on 3/28/17.
 */
class BulletHellVsGame(

) : AbstractImmutableGameModelCollection<GameController, GameView, GameModel<GameController, GameView>>() {
    override val collection = HashSet<GameModel<GameController, GameView>>()

    val camera = OrthographicCameraEntity().apply {
        zoom = 20f
    }

    init {
        collection.add(camera)

        //background and camera update
        collection.add(object : GameModel<GameController, GameView> {
            override fun generateController(dependency: GameController): Disposable = EmptyDisposable

            override fun generateView(dependency: GameView): Disposable = makeDisposable {
                val batchingRenderer = dependency.services.get<BatchingRenderer>().aside { add(it) }.service
                listen(dependency.render, -100) {
                    Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
                    Gdx.gl.glClear(
                            GL20.GL_COLOR_BUFFER_BIT
                                    or GL20.GL_DEPTH_BUFFER_BIT
                                    or if (Gdx.graphics.bufferFormat.coverageSampling)
                                GL20.GL_COVERAGE_BUFFER_BIT_NV
                            else
                                0
                    )
                    batchingRenderer.matrix = camera.combined
                }
            }
        })
    }
}