package com.ivieleague.service

/**
 * Created by josep on 1/10/2017.
 */
inline fun <reified T : Any> MutableMap<String, in T>.getType(key: String, maker: () -> T): T
        = getOrPut(key, maker) as T

inline fun <reified T : Any> MutableMap<String, in T>.getType(maker: () -> T): T
        = getOrPut(T::class.java.name, maker) as T

inline fun <reified T : Any> MutableMap<String, in T>.getType(): T
        = getOrPut(T::class.java.name, { T::class.java.getDeclaredConstructor().newInstance() }) as T