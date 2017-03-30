package com.ivieleague.rendering

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.EarClippingTriangulator
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import java.util.*

/**
 * Created by josep on 1/9/2017.
 */
class InMemoryMesh(
        var vertexSize: Int = 4,
        var dimensions: Int = 3,
        var vertices: FloatArray,
        var indices: ShortArray
) {
    private inline fun ShortArray.modify(amount: Int): ShortArray = ShortArray(this.size) {
        (this[it] + amount).toShort()
    }

    operator fun plus(other: InMemoryMesh): InMemoryMesh {
        assert(vertexSize == other.vertexSize)
        assert(dimensions == other.dimensions)
        return InMemoryMesh(vertexSize, dimensions, vertices + other.vertices, indices + other.indices.modify(vertices.size / vertexSize))
    }

    fun duplicate():InMemoryMesh {
        val otherVertices = FloatArray(vertices.size)
        val otherIndices = ShortArray(indices.size)
        System.arraycopy(vertices, 0, otherVertices, 0, vertices.size)
        System.arraycopy(indices, 0, otherIndices, 0, indices.size)
        return InMemoryMesh(vertexSize, dimensions, otherVertices, otherIndices)
    }

    fun set(other:InMemoryMesh){
        assert(vertexSize == other.vertexSize)
        assert(dimensions == other.dimensions)
        assert(vertices.size == other.vertices.size)
        assert(indices.size == other.indices.size)
        System.arraycopy(other.vertices, 0, vertices, 0, other.vertices.size)
        System.arraycopy(other.indices, 0, indices, 0, other.indices.size)
    }

    infix fun transform(matrix: Matrix4) = duplicate().apply{
        transformAssign(matrix)
    }
    fun transformAssign(matrix: Matrix4) {
        if (dimensions < 1 || dimensions > vertexSize) throw IndexOutOfBoundsException()

        val tmp = Vector3()

        var idx = 0
        when (dimensions) {
            1 -> while (idx + vertexSize <= vertices.size) {
                tmp.set(vertices[idx], 0f, 0f).mul(matrix)
                vertices[idx] = tmp.x
                idx += vertexSize
            }
            2 -> while (idx + vertexSize <= vertices.size) {
                tmp.set(vertices[idx], vertices[idx + 1], 0f).mul(matrix)
                vertices[idx] = tmp.x
                vertices[idx + 1] = tmp.y
                idx += vertexSize
            }
            3 -> while (idx + vertexSize <= vertices.size) {
                tmp.set(vertices[idx], vertices[idx + 1], vertices[idx + 2]).mul(matrix)
                vertices[idx] = tmp.x
                vertices[idx + 1] = tmp.y
                vertices[idx + 2] = tmp.z
                idx += vertexSize
            }
        }
    }

    infix fun translate(by: Vector3) = duplicate().apply{
        translateAssign(by)
    }
    fun translateAssign(by: Vector3) {
        if (dimensions < 1 || dimensions > vertexSize) throw IndexOutOfBoundsException()

        var idx = 0
        when (dimensions) {
            1 -> while (idx + vertexSize <= vertices.size) {
                vertices[idx] += by.x
                idx += vertexSize
            }
            2 -> while (idx + vertexSize <= vertices.size) {
                vertices[idx] += by.x
                vertices[idx + 1] += by.y
                idx += vertexSize
            }
            3 -> while (idx + vertexSize <= vertices.size) {
                vertices[idx] += by.x
                vertices[idx + 1] += by.y
                vertices[idx + 2] += by.z
                idx += vertexSize
            }
        }
    }

    infix fun transformColorAssign(
            colorTransform: (Float) -> Float
    ) {
        if (dimensions < 1 || dimensions > vertexSize) throw IndexOutOfBoundsException()

        var idx = 0
        while (idx + vertexSize <= vertices.size) {
            val current = vertices[idx + dimensions]
            vertices[idx + dimensions] = colorTransform(current)
            idx += vertexSize
        }
    }

    class Builder(
            val vertices: ArrayList<Vertex> = ArrayList<Vertex>(),
            val indices: ArrayList<Short> = ArrayList<Short>()
    ) {
        class Vertex(var x: Float, var y: Float, var z: Float, var color: Float)

        fun vertex(x: Float, y: Float, z: Float, color: Float) {
            vertices.add(Vertex(x, y, z, color))
        }

        fun vertex(x: Float, y: Float, z: Float, color: Color) {
            vertices.add(Vertex(x, y, z, color.toFloatBits()))
        }

        fun vertex(v: Vector2, z: Float, color: Color) {
            vertices.add(Vertex(v.x, v.y, z, color.toFloatBits()))
        }

        fun vertex(v: Vector3, color: Color) {
            vertices.add(Vertex(v.x, v.y, v.z, color.toFloatBits()))
        }

        fun vertex(x: Float, y: Float, z: Float, r: Float, g: Float, b: Float, a: Float) {
            vertices.add(Vertex(x, y, z, Color.toFloatBits(r, g, b, a)))
        }

        fun vertex(x: Float, y: Float, z: Float, r: Int, g: Int, b: Int, a: Int) {
            vertices.add(Vertex(x, y, z, Color.toFloatBits(r, g, b, a)))
        }

        fun index(index: Int) {
            indices.add(index.toShort())
        }

        fun triangle(indexA: Int, indexB: Int, indexC: Int) {
            indices.add(indexA.toShort())
            indices.add(indexB.toShort())
            indices.add(indexC.toShort())
        }

        fun build(): InMemoryMesh {
            return InMemoryMesh(
                    4,
                    3,
                    FloatArray(vertices.size * 4).apply {
                        var currentIndex = 0
                        for (vertex in vertices) {
                            this[currentIndex++] = vertex.x
                            this[currentIndex++] = vertex.y
                            this[currentIndex++] = vertex.z
                            this[currentIndex++] = vertex.color
                        }
                    },
                    indices.toShortArray()
            )
        }
    }

    companion object {

        inline fun build(builder: Builder.() -> Unit) = Builder().apply(builder).build()

        fun triangle(
                a: Vector3,
                b: Vector3,
                c: Vector3,
                color: Color
        ): InMemoryMesh = build {
            vertex(a, color)
            vertex(b, color)
            vertex(c, color)
            triangle(0, 1, 2)
        }

        fun triangle(
                a: Vector2,
                b: Vector2,
                c: Vector2,
                z: Float = 0f,
                color: Color
        ): InMemoryMesh = build {
            vertex(a, z, color)
            vertex(b, z, color)
            vertex(c, z, color)
            triangle(0, 1, 2)
        }

        fun rectangle(
                center: Vector2,
                width: Float,
                height: Float,
                z: Float = 0f,
                color: Color
        ): InMemoryMesh = build {
            vertex(center.x - width * .5f, center.y - height * .5f, z, color)
            vertex(center.x + width * .5f, center.y - height * .5f, z, color)
            vertex(center.x - width * .5f, center.y + height * .5f, z, color)
            vertex(center.x + width * .5f, center.y + height * .5f, z, color)

            triangle(0, 3, 1)
            triangle(0, 2, 3)
        }

        fun polygon(
                points: List<Vector2>,
                z: Float = 0f,
                color: Color
        ): InMemoryMesh {
            val tris = FloatArray(points.size * 2)
            var i = 0
            for (pt in points) {
                tris[i++] = pt.x
                tris[i++] = pt.y
            }
            val indices = EarClippingTriangulator().computeTriangles(tris).toArray()
            val vertices = FloatArray(points.size * 4)
            i = 0
            for (pt in points) {
                vertices[i++] = pt.x
                vertices[i++] = pt.y
                vertices[i++] = z
                vertices[i++] = color.toFloatBits()
            }
            return InMemoryMesh(4, 3, vertices, indices)
        }

        fun circle(
                center: Vector2,
                z: Float = 0f,
                radius: Float,
                color: Color,
                points: Int = 12,
                startAngle: Double = 0.0
        ) = build {
            for (index in 1..points) {
                val angle = startAngle + Math.PI * 2 * (index / points.toDouble())
                vertex(
                        Math.cos(angle).toFloat() * radius + center.x,
                        Math.sin(angle).toFloat() * radius + center.y,
                        z,
                        color
                )
            }
            for (i in 1..points - 2) {
                triangle(0, i + 1, i)
            }
        }
    }
}