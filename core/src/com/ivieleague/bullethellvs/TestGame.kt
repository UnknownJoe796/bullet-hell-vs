package com.ivieleague.bullethellvs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.ivieleague.disposable.EmptyDisposable
import com.ivieleague.disposable.makeDisposable
import com.ivieleague.event.listen
import com.ivieleague.kotlin.aside
import com.ivieleague.rendering.BatchingRenderer
import com.ivieleague.rendering.InMemoryMesh
import com.lightningkite.kotlin.Disposable
import java.util.*

class TestGame : AbstractImmutableGameModelCollection<GameController, GameView, GameModel<GameController, GameView>>() {
    override val collection = HashSet<GameModel<GameController, GameView>>()

    val camera = OrthographicCameraEntity().apply {
        zoom = 20f
    }

    init {
        collection.add(camera)

        //background and camera
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

        collection.add(Player())

//        //moving object
//        collection.add(object : GameModel<GameController, GameView> {
//            val position = Vector2(0f, 0f)
//
//            override fun generateController(dependency: GameController): Disposable = makeDisposable {
//                listen(dependency.step, 1) { time ->
//                    if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
//                        position.x -= time * 50f
//                    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
//                        position.x += time * 50f
//                    if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
//                        position.y -= time * 50f
//                    if (Gdx.input.isKeyPressed(Input.Keys.UP))
//                        position.y += time * 50f
//                }
//            }
//
//            override fun generateView(dependency: GameView): Disposable = makeDisposable {
//                val img = Texture("badlogic.jpg").aside { add(it.disposable) }
//                listen(dependency.render, 4) { time ->
//                    val batch = dependency.services.getType<SpriteBatch>()
//                    batch.begin()
//                    batch.draw(
//                            img,
//                            position.x,
//                            position.y
//                    )
//                    batch.end()
//                }
//            }
//        })

        //test mesh
        collection.add(object : GameModel<GameController, GameView> {
            val mesh = InMemoryMesh.circle(Vector2(150f, 150f), 0f, 100f, Color.GOLD, 24)

            override fun generateController(dependency: GameController): Disposable = makeDisposable {
                //                listen(dependency.step, 2) { time ->
//                    //mutate
//                    for (i in 0..(mesh.vertices.size / 4) - 1) {
//                        mesh.vertices[i * 4] += (Math.random() * 2.0 - 1.0).toFloat().times(.2f)
//                        mesh.vertices[i * 4 + 1] += (Math.random() * 2.0 - 1.0).toFloat().times(.2f)
//                    }
//                }
            }

            override fun generateView(dependency: GameView): Disposable = makeDisposable {
                val batchingRenderer = dependency.services.get<BatchingRenderer>().aside { add(it) }.service

                listen(dependency.render, 6) { time ->
                    batchingRenderer.matrix = Matrix4().apply {
                        setToOrtho2D(0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
                    }
                    batchingRenderer.append(mesh)
                    batchingRenderer.flush()
                }
            }
        })
    }
}
