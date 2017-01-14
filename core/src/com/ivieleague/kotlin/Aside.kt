package com.ivieleague.kotlin

/**
 * Created by josep on 1/10/2017.
 */
inline fun <T> T.aside(action: (T) -> Unit): T {
    action(this)
    return this
}