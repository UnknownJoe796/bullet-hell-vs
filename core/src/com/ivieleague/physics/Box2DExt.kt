package com.ivieleague.physics

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*

var Body.pos: Vector2
    get() = position
    set(value) = setTransform(value, angle)
var Body.rotation: Float
    get() = angle
    set(value) = setTransform(position, value)
var Body.x: Float
    get() = position.x
    set(value) = setTransform(value, y, angle)
var Body.y: Float
    get() = position.y
    set(value) = setTransform(x, value, angle)
var Body.vx: Float
    get() = linearVelocity.x
    set(value) = setLinearVelocity(value, vy)
var Body.vy: Float
    get() = linearVelocity.y
    set(value) = setLinearVelocity(vx, value)

fun Body.getTransformMatrix(matrix: Matrix4): Matrix4 {
    val transform = transform
    matrix.idt()
    matrix.translate(transform.position.x, transform.position.y, 0f)
    matrix.rotate(0f, 0f, 1f, (transform.rotation * 180 / Math.PI).toFloat())
    return matrix
}

inline fun World.makeBody(init: BodyDef.() -> Unit): Body {
    val def = BodyDef()
    def.init()
    return createBody(def)
}

inline fun World.makeJoint(init: JointDef.() -> Unit): Joint {
    val def = JointDef()
    def.init()
    return createJoint(def)
}

inline fun Body.addFixture(init: FixtureDef.() -> Unit): Body {
    val def = FixtureDef()
    def.init()
    createFixture(def)
    return this
}

inline fun Body.addFixture(shape:Shape, init: FixtureDef.() -> Unit): Body {
    val def = FixtureDef()
    def.init()
    def.shape = shape
    createFixture(def)
    shape.dispose()
    return this
}

inline fun Body.makeFixture(init: FixtureDef.() -> Unit): Fixture {
    val def = FixtureDef()
    def.init()
    return createFixture(def)
}

fun Body.makeDef(): BodyDef {
    val def = BodyDef()
    def.active = isActive
    def.allowSleep = isSleepingAllowed
    def.angle = angle
    def.angularDamping = angularDamping
    def.angularVelocity = angularVelocity
    def.awake = isAwake
    def.bullet = isBullet
    def.fixedRotation = isFixedRotation
    def.gravityScale = gravityScale
    def.linearDamping = linearDamping
    def.linearVelocity.set(linearVelocity)
    def.position.set(position)
    def.type = type
    return def
}

fun Fixture.makeDef(): FixtureDef {
    val def = FixtureDef()
    def.density = density
    def.filter.categoryBits = filterData.categoryBits
    def.filter.groupIndex = filterData.groupIndex
    def.filter.maskBits = filterData.maskBits
    def.friction = friction
    def.isSensor = isSensor
    def.restitution = restitution
    def.shape = shape
    return def
}

fun Body.makeFixtureDefs(): Array<FixtureDef> {
    val fixtures = fixtureList
    val defs = Array(fixtures.size) { index -> fixtures[index].makeDef() }
    return defs
}

fun Body.makeFixtures(defs: Array<FixtureDef>): Array<Fixture> {
    return Array(defs.size) { index -> createFixture(defs[index]) }
}

val World.bodies: com.badlogic.gdx.utils.Array<Body?>
    get() {
        val bodies = com.badlogic.gdx.utils.Array<Body?>(bodyCount)
        getBodies(bodies)
        return bodies
    }

fun World.explosion(explosionCenter: Vector2, strength: Float) {
    for (body in bodies) {
        if (body == null
                || body.type != BodyDef.BodyType.DynamicBody) {
            continue
        }

        val dx = body.x - explosionCenter.x
        val dy = body.y - explosionCenter.y

        var dist = dx * dx + dy * dy
        if (dist < .25f) {
            dist = 1f
        }
        val scale = strength / dist
        body.applyLinearImpulse(
                dx * scale,
                dy * scale,
                body.x,
                body.y,
                true
        )
    }
}