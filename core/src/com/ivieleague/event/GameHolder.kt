package com.ivieleague.event

import com.badlogic.gdx.Gdx
import com.ivieleague.bullethellvs.GameController
import com.ivieleague.bullethellvs.GameModel
import com.ivieleague.bullethellvs.GameView
import com.ivieleague.service.ServiceBroker
import com.lightningkite.kotlin.Disposable
import com.lightningkite.kotlin.runAll
import java.util.*

/**
 * Created by josep on 1/10/2017.
 */
class GameHolder(val game: GameModel<GameController, GameView>) : ApplicationEvents() {
    val step: TreeSet<PriorityListener1<Float>> = TreeSet()
    val render: TreeSet<PriorityListener1<Float>> = TreeSet()

    val gameController = object: GameController{
        override val services: ServiceBroker<GameController> = ServiceBroker(this)
        override val step: TreeSet<PriorityListener1<Float>>
            get() = this@GameHolder.step
    }
    val gameView = object: GameView{
        override val services: ServiceBroker<GameView> = ServiceBroker(this)
        override val render: TreeSet<PriorityListener1<Float>>
            get() = this@GameHolder.render
        override val resize: TreeSet<PriorityListener2<Int, Int>>
            get() = this@GameHolder.onResize
    }

    var controller: Disposable? = null
    var view: Disposable? = null

    override fun create() {
        controller = game.generateController(gameController)
        view = game.generateView(gameView)
    }

    override fun render() {
        super.render()
        step.runAll(Gdx.graphics.deltaTime)
        render.runAll(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        controller?.dispose()
    }
}