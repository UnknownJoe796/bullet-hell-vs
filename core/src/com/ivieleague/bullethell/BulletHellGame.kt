package com.ivieleague.bullethell

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
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

    val playerOne = Player()
    val playerTwo = Player()

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
        world.events.get<Player.DeathEvent>() += {
            reset()
        }
    }

    override fun create() {
    }

    override fun render() {
        val controllers = Controllers.getControllers()
        val controllerOne = controllers.firstOrNull()
        val controllerTwo = if (controllers.size >= 2) controllers[1] else null
        val joy = Vector2()

        //Player One
        joy.set(0f, 0f)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            joy.x -= 1
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            joy.x += 1
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            joy.y += 1
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            joy.y -= 1

        if (controllerOne != null) {
            joy += Vector2(controllerOne.getAxis(0), -controllerOne.getAxis(3))
        }

        playerOne.move(joy)

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            playerOne.shoot(world)
        }

        for (buttonIndex in 0..12) {
            if (controllerOne != null && controllerOne.getButtonJustPressed(buttonIndex)) {
                playerOne.shoot(world, buttonIndex)
            }
        }

        //Player two
        joy.set(0f, 0f)
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            joy.x -= 1
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            joy.x += 1
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            joy.y += 1
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            joy.y -= 1

        if (controllerTwo != null) {
            joy += Vector2(-controllerTwo.getAxis(0), controllerTwo.getAxis(3))
        }

        playerTwo.move(joy)

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            playerTwo.shoot(world)
        }

        for (buttonIndex in 0..12) {
            if (controllerTwo != null && controllerTwo.getButtonJustPressed(buttonIndex)) {
                playerTwo.shoot(world, buttonIndex)
            }
        }

        //Controller Cleanup

        controllerOne?.flip()
        controllerTwo?.flip()



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