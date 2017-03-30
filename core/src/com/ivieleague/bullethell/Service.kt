package com.ivieleague.bullethell

interface Service {
    fun beginStep()

    /**
     * @return Whether or not the service should be removed.
     */
    fun endStep(): Boolean
}