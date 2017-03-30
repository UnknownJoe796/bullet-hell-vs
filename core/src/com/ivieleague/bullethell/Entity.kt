package com.ivieleague.bullethell

interface Entity : Comparable<Entity> {
    val depth: Int
    override fun compareTo(other: Entity): Int {
        if (this == other) return 0
        val res = depth.compareTo(other.depth)
        return if (res != 0) res else hashCode().compareTo(other.hashCode())
    }

    /**
     * @return Whether or not the entity should be removed
     */
    fun step(world: World, time: Float)

    fun draw(view: WorldView) {}
}