package com.ivieleague.event

import com.ivieleague.zeldarando.GameController
import com.ivieleague.zeldarando.GameModel
import com.ivieleague.zeldarando.GameView
import com.lightningkite.kotlin.Disposable
import java.util.*

/**
 * Created by josep on 1/10/2017.
 */
class GameHolder(val game: GameModel<GameController, GameView>) : ApplicationEvents(), GameController, GameView {
    override val services: MutableMap<String, Any> = HashMap()
    override val step: TreeSet<PriorityListener1<Float>> get() = onRender
    override val render: TreeSet<PriorityListener1<Float>> get() = onRender

    var controller: Disposable? = null
    var view: Disposable? = null

    override fun create() {
        controller = game.generateController(this)
        view = game.generateView(this)
    }

    override fun dispose() {
        controller?.dispose()
    }
}