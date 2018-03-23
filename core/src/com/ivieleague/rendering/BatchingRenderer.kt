package com.ivieleague.rendering

import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import java.io.Closeable
import java.util.*

/**
 * Created by josep on 1/9/2017.
 */
open class BatchingRenderer(
        val numVerticies: Int = 4096,
        val numIndices: Int = 4096,
        val shapeType: ShapeRenderer.ShapeType = ShapeRenderer.ShapeType.Filled,
        val vertexAttributes: VertexAttributes = BatchingRenderer.buildVertexAttributes(false, true, 0),
        val shader: ShaderProgram = ImmediateModeRenderer20.createDefaultShader(false, true, 0),
        val deleteShaderAtEnd: Boolean = true
) : Closeable {
    val vertices = FloatArray(numVerticies)
    val indices = ShortArray(numIndices)
    var currentVertexPosition = 0
    var currentIndexPosition = 0
    var matrix: Matrix4 = Matrix4()

    val mesh: Mesh = Mesh(false, numVerticies, numIndices, vertexAttributes)

    val numberOfTextures = vertexAttributes.count { it.usage == VertexAttributes.Usage.TextureCoordinates }

    fun append(subvertices: FloatArray, subindices: ShortArray) {
        flushIfCannotContain(subvertices.size, subindices.size)
        var i = currentIndexPosition
        for (index in subindices) {
            indices[i] = (index + currentVertexPosition).toShort()
            i++
        }
        currentIndexPosition += subindices.size
        System.arraycopy(subvertices, 0, vertices, currentVertexPosition, subvertices.size)
        currentVertexPosition += subvertices.size
    }

    fun append(inMemoryMesh: InMemoryMesh) {
        flushIfCannotContain(inMemoryMesh.vertices.size, inMemoryMesh.indices.size)
        var i = currentIndexPosition
        val startingOffset = currentVertexPosition / (vertexAttributes.vertexSize / 4)
        for (index in inMemoryMesh.indices) {
            indices[i] = (index + startingOffset).toShort()
            i++
        }
        currentIndexPosition += inMemoryMesh.indices.size
        System.arraycopy(inMemoryMesh.vertices, 0, vertices, currentVertexPosition, inMemoryMesh.vertices.size)
        currentVertexPosition += inMemoryMesh.vertices.size
    }

    inline fun flushIfCannotContain(subverticesCount: Int, subindicesCount: Int) {
        if (currentIndexPosition + subindicesCount >= numIndices) {
            if (subindicesCount >= numIndices) throw IllegalArgumentException()
            if (subverticesCount >= numVerticies) throw IllegalArgumentException()
            flush()
        } else if (currentVertexPosition + subverticesCount >= numVerticies) {
            if (subindicesCount >= numIndices) throw IllegalArgumentException()
            if (subverticesCount >= numVerticies) throw IllegalArgumentException()
            flush()
        }
    }

    fun flush() {
        if (currentIndexPosition == 0) return
        shader.begin()
        shader.setUniformMatrix("u_projModelView", matrix)
        for (i in 0..numberOfTextures - 1)
            shader.setUniformi("u_sampler" + i, i)
        mesh.setVertices(vertices, 0, currentVertexPosition)
        mesh.setIndices(indices, 0, currentIndexPosition)
        mesh.render(shader, shapeType.glType)
        shader.end()

        currentVertexPosition = 0
        currentIndexPosition = 0
    }

    override fun close() {
        mesh.dispose()
        if (deleteShaderAtEnd) shader.dispose()
    }

    companion object {
        fun buildVertexAttributes(hasNormals: Boolean, hasColor: Boolean, numTexCoords: Int): VertexAttributes {
            val attribs = ArrayList<VertexAttribute>()
            attribs.add(VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE))
            if (hasNormals) attribs.add(VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE))
            if (hasColor) attribs.add(VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE))
            for (i in 0..numTexCoords - 1) {
                attribs.add(VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + i))
            }
            return VertexAttributes(*attribs.toTypedArray())
        }
    }
}