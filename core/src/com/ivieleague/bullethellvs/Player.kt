package com.ivieleague.bullethellvs

import com.badlogic.gdx.math.Vector2
import com.ivieleague.disposable.makeDisposable
import com.ivieleague.event.listen
import com.ivieleague.kotlin.plusAssign
import com.ivieleague.kotlin.times
import com.lightningkite.kotlin.Disposable

/**
 * Created by joseph on 3/28/17.
 */
class Player() : GameModel<GameController, GameView> {

    val position = Vector2()
    val velocity = Vector2()

    override fun generateController(dependency: GameController): Disposable = makeDisposable {
        listen(dependency.step, 0) { time: Float ->
            position += velocity * time
        }
    }

    override fun generateView(dependency: GameView): Disposable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}