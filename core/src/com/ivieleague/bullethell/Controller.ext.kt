package com.ivieleague.bullethell

import com.badlogic.gdx.controllers.Controller
import java.util.*

/**
 *
 * Created by joseph on 3/28/17.
 */

private val buttons = WeakHashMap<Controller, HashMap<Int, Boolean>>()

fun Controller.getButtonPrevious(button: Int): Boolean
        = buttons.getOrPut(this) { HashMap() }.getOrPut(button) { false }

fun Controller.flip() {
    for (entry in buttons.getOrPut(this) { HashMap() }) {
        entry.setValue(getButton(entry.key))
    }
}

fun Controller.getButtonJustPressed(button: Int) = getButton(button) && !getButtonPrevious(button)