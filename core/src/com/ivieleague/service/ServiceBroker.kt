package com.ivieleague.service

import com.lightningkite.kotlin.Disposable
import java.util.*

/**
 * Created by josep on 1/19/2017.
 */
inline fun <reified T: Any> ServiceBroker(dependency:T):ServiceBroker<T>
        = ServiceBroker(T::class.java, dependency)

class ServiceBroker<Dependency>(
        val dependencyType: Class<Dependency>,
        val dependency: Dependency
) {
    private class ServiceContainer(
            val key:String,
            val service: Disposable,
            var references: Int = 0
    )
    interface ServiceHook<T> : Disposable {
        val service: T
    }
    private val map = HashMap<String, ServiceContainer>()


    inner private class MyServiceHook<T>(val container:ServiceContainer) : ServiceHook<T> {
        @Suppress("UNCHECKED_CAST")
        override val service: T
            get() = container.service as T

        init{
            container.references++
        }
        override fun dispose() {
            container.references--
            if(container.references <= 0){
                //remove service
                map.remove(container.key)
                container.service.dispose()
            }
        }
    }


    inline fun <reified T : Disposable> get(): ServiceHook<T> = get(T::class.java)
    fun <T : Disposable> get(classObj:Class<T>): ServiceHook<T> {
        val key = classObj.name
        val existing = map[key]
        if(existing != null) return MyServiceHook(existing)

        val newService = try{
            classObj.getDeclaredConstructor(dependencyType).newInstance(dependency)
        } catch(e:NoSuchMethodException){
            classObj.getDeclaredConstructor().newInstance()
        }
        val new = ServiceContainer(key, newService)
        map[key] = new
        return MyServiceHook(new)
    }
}