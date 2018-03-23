package com.ivieleague.bullethell.lib

interface Service {
    fun beginStep()

    /**
     * @return Whether or not the service should be removed.
     */
    fun endStep(): Boolean
}