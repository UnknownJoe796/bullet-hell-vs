package com.ivieleague.bullethellvs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.ivieleague.disposable.EmptyDisposable
import com.ivieleague.disposable.makeDisposable
import com.ivieleague.event.listen
import com.lightningkite.kotlin.Disposable

/**
 * Created by josep on 1/17/2017.
 */
class OrthographicCameraEntity : OrthographicCamera(), GameModel<GameController, GameView> {

    override fun generateController(dependency: GameController): Disposable = EmptyDisposable

    override fun generateView(dependency: GameView): Disposable = makeDisposable {
        listen(dependency.resize, Int.MIN_VALUE) { width, height ->
            viewportWidth = width * 1f / height
            viewportHeight = 1f
            update()
        }
        viewportWidth = Gdx.graphics.width * 1f / Gdx.graphics.height
        viewportHeight = 1f
        update()
    }
}