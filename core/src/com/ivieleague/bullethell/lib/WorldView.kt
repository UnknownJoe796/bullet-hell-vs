package com.ivieleague.bullethell.lib

import com.badlogic.gdx.math.Matrix4
import java.util.*

class WorldView {
    val matrix: Matrix4 = Matrix4()
    val services: HashMap<String, Service> = HashMap()
    fun draw(entities: Collection<Entity>) {
        services.values.forEach { it.beginStep() }
        entities.forEach { it.draw(this) }
        services.values.removeAll { it.endStep() }
    }
}