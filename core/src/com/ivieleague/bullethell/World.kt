package com.ivieleague.bullethell

import java.util.*
import java.util.concurrent.ConcurrentSkipListSet

class World {
    val events: Events = Events()
    val entities: ConcurrentSkipListSet<Entity> = ConcurrentSkipListSet<Entity>()
    val services: HashMap<String, Service> = HashMap()
    private var breakLoop = false

    fun step(time: Float) {
        services.values.forEach { it.beginStep() }
        for (it in entities) {
            it.step(this, time)
            if (breakLoop) break
        }
        breakLoop = false
        services.values.removeAll { it.endStep() }
    }

    fun clear() {
        entities.clear()
        breakLoop = true
    }
}