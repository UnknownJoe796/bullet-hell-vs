package com.ivieleague.bullethell.playerschemes

import com.ivieleague.bullethell.entities.PlayerController
import com.ivieleague.bullethell.entities.PlayerInterface
import com.ivieleague.kotlin.Vector2_polar
import lk.kotlin.jvm.utils.random.random

class TotalControlScheme : PlayerController {

    fun defaultShotVel() = Vector2_polar(10f, (-.25...25).random())

    override fun invoke(ship: PlayerInterface, time: Float) = with(ship) {
    }
}