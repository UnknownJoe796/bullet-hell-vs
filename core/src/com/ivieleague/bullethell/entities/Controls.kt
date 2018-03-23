package com.ivieleague.bullethell.entities

import com.badlogic.gdx.math.Vector2

class Controls {
    companion object {
        const val BUTTON_COUNT = 10
    }

    val joystick = Vector2()
    val buttons = BooleanArray(BUTTON_COUNT) { false }
    val buttonsPrevious = BooleanArray(BUTTON_COUNT) { false }

    fun flip() {
        joystick.set(0f, 0f)
        System.arraycopy(buttons, 0, buttonsPrevious, 0, BUTTON_COUNT)
        buttons.indices.forEach { buttons[it] = false }
    }

    fun buttonJustPressed(index: Int) = buttons[index] && !buttonsPrevious[index]

    override fun toString(): String {
        return "Buttons: ${buttons.joinToString()}"
    }
}