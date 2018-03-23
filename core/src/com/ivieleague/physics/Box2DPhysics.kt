package com.ivieleague.physics

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import java.io.Closeable
import java.util.*

/**
 * Created by josep on 1/9/2017.
 */

class Box2DPhysics() : Closeable, ContactListener {

    init {
        Box2D.init()
    }

    val world: World = World(Vector2.Zero, true).apply {
        setContactListener(this@Box2DPhysics)
    }

    var timeStep: Float = -1f
    var positionIterations: Int = 5
    var velocityIterations: Int = 5

    val map: HashMap<Body, Module> = HashMap()
    val staticBody = world.makeBody {
        type = BodyDef.BodyType.StaticBody
    }

    fun step(time: Float) {
        world.step(if (timeStep == -1f) time else timeStep, velocityIterations, positionIterations)
    }

    override fun close() {
        map.clear()
        world.dispose()
    }

    //contact listener
    override fun postSolve(contact: Contact, impulse: ContactImpulse) {
        val moduleA = map[contact.fixtureA.body]
        val moduleB = map[contact.fixtureB.body]
        moduleA?.listener?.postSolve(moduleB!!, contact, impulse)
        moduleB?.listener?.postSolve(moduleA!!, contact, impulse)
    }

    override fun beginContact(contact: Contact) {
        val moduleA = map[contact.fixtureA.body]
        val moduleB = map[contact.fixtureB.body]
        moduleA?.listener?.beginContact(moduleB!!, contact)
        moduleB?.listener?.beginContact(moduleA!!, contact)
    }

    override fun endContact(contact: Contact) {
        val moduleA = map[contact.fixtureA.body]
        val moduleB = map[contact.fixtureB.body]
        moduleA?.listener?.endContact(moduleB!!, contact)
        moduleB?.listener?.endContact(moduleA!!, contact)
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        val moduleA = map[contact.fixtureA.body]
        val moduleB = map[contact.fixtureB.body]
        moduleA?.listener?.preSolve(moduleB!!, contact, oldManifold)
        moduleB?.listener?.preSolve(moduleA!!, contact, oldManifold)
    }

    inline fun body(
            owner: Any?,
            listener: ContactListener?,
            creator: World.() -> Body
    ): Module = Module(this, owner, world.creator(), listener)

    class Module(
            val physics: Box2DPhysics,
            val owner: Any?,
            val body: Body,
            val listener: ContactListener? = null
    ) : Closeable {
        init {
            physics.map[body] = this
        }

        override fun close() {
            physics.map.remove(body)
            physics.world.destroyBody(body)
        }
    }

    interface ContactListener {
        fun beginContact(other: Module, contact: Contact)
        fun endContact(other: Module, contact: Contact)
        fun preSolve(other: Module, contact: Contact, oldManifold: Manifold)
        fun postSolve(other: Module, contact: Contact, impulse: ContactImpulse)
    }
}