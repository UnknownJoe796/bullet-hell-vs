package com.ivieleague.bullethell.lib

import com.badlogic.gdx.math.Matrix4
import com.ivieleague.rendering.BatchingRenderer

class BatchingRendererService(val matrix: Matrix4) : Service {

    val batchingRenderer = BatchingRenderer()

    override fun beginStep() {
        batchingRenderer.matrix = matrix
    }

    override fun endStep(): Boolean {
        batchingRenderer.flush()
        return false
    }
}