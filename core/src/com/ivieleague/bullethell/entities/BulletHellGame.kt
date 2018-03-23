package com.ivieleague.bullethell.entities

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.ivieleague.bullethell.lib.World
import com.ivieleague.bullethell.lib.WorldView
import com.ivieleague.bullethell.playerschemes.BradysScheme
import com.ivieleague.bullethell.playerschemes.TotalControlScheme
import com.ivieleague.kotlin.plusAssign

/**
 *
 * Created by joseph on 3/28/17.
 */
class BulletHellGame() : ApplicationListener {

    val worldSize = 60f

    val camera = OrthographicCamera().apply {
        zoom = worldSize
    }

    val world = World()
    val view = WorldView()

    val playerOne = Player(BradysScheme())
    val playerTwo = Player(TotalControlScheme())

        fun reset() {
        println("RESET")
        world.clear()
        world.entities += Background(Color.BLACK)
        world.entities += playerOne.apply {
            reset()
            position.x = 0f
            position.y = -20f
            angle = Math.PI.toFloat() / 2f
            color = Color.RED
        }
        world.entities += playerTwo.apply {
            reset()
            position.x = 0f
            position.y = 20f
            angle = -Math.PI.toFloat() / 2f
            color = Color.BLUE
        }
    }

    init {
        reset()
    }

    override fun create() {
    }

    data class PlayerControllerInfo(
            val player: Player,
            val controller: Controller?,
            val rotateJoy: Double
    )

    override fun render() {

        //Controls
        val controllers = Controllers.getControllers()
        val controllerOne = controllers.firstOrNull()
        val controllerTwo = if (controllers.size >= 2) controllers[1] else null
        val playerControllerInfos = listOf(
                PlayerControllerInfo(
                        player = playerOne,
                        controller = controllerOne,
                        rotateJoy = 0.0
                ),
                PlayerControllerInfo(
                        player = playerOne,
                        controller = controllerOne,
                        rotateJoy = Math.PI
                )
        )

        for (info in playerControllerInfos) {
            info.player.controls.flip()

            if (info.controller == null) continue

            info.player.controls.joystick += Vector2(info.controller.getAxis(0), -info.controller.getAxis(3)).rotateRad(info.rotateJoy.toFloat())

            for (index in 0 until Controls.BUTTON_COUNT) {
                info.player.controls.buttons[index] = info.controller.getButton(index)
            }
        }

        //debug controls
        run {
            val controls = playerOne.controls
            val joy = controls.joystick

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                joy.x -= 1
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                joy.x += 1
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
                joy.y += 1
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                joy.y -= 1

            for (button in Input.Keys.NUM_1..Input.Keys.NUM_9) {
                if (Gdx.input.isKeyPressed(button)) {
                    controls.buttons[button - Input.Keys.NUM_1] = true
                }
            }
        }

        //Reset
        if (world.entities.count { it is Player } <= 1) {
            for (info in playerControllerInfos) {
                if (info.player.controls.buttons[9]) {
                    reset()
                }
            }
        }

        world.step(Gdx.graphics.deltaTime.coerceAtMost(.25f))

        camera.viewportWidth = Gdx.graphics.width * 1f / Gdx.graphics.height
        camera.viewportHeight = 1f
        camera.update()
        view.matrix.set(camera.combined)

        view.draw(world.entities)
    }

    override fun dispose() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        camera.viewportWidth = width * 1f / height
        camera.viewportHeight = 1f
        camera.update()
        view.matrix.set(camera.combined)
    }

}