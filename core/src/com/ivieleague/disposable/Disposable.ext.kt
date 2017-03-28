package com.ivieleague.disposable

import com.lightningkite.kotlin.Disposable
import java.util.*

/**
 * Created by josep on 1/10/2017.
 */

class CollectionItemDisposable<E>(val collection: MutableCollection<E>, val item: E) : Disposable {
    init {
        collection.add(item)
    }

    override fun dispose() {
        collection.remove(item)
    }
}

fun <E> MutableCollection<Disposable>.listen(
        collection: MutableCollection<E>,
        item: E
) = add(CollectionItemDisposable(collection, item))

fun <E> MutableCollection<Disposable>.removeAndDispose(
        disposable: Disposable
) {
    remove(disposable)
    disposable.dispose()
}

fun Collection<Disposable>.dispose() = forEach { it.dispose() }
fun Map<*, Disposable>.dispose() = values.forEach { it.dispose() }

val Collection<Disposable>.disposable: Disposable get() = object : Disposable {
    override fun dispose() {
        forEach { it.dispose() }
    }
}

val com.badlogic.gdx.utils.Disposable.disposable: Disposable get() = object : Disposable {
    override fun dispose() = this@disposable.dispose()
}

interface DisposableCollection : Collection<Disposable>, Disposable {
    override fun dispose() {
        forEach { it.dispose() }
    }
}
object EmptyDisposable: Disposable{
    override fun dispose() {}
}
class ImmutableDisposableCollection(of: Collection<Disposable>) : DisposableCollection, Collection<Disposable> by of
class MutableDisposableCollection(of: MutableCollection<Disposable> = HashSet()) : DisposableCollection, MutableCollection<Disposable> by of

fun makeDisposable(action: MutableDisposableCollection.() -> Unit): MutableDisposableCollection = MutableDisposableCollection().apply(action)