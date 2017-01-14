package com.ivieleague.zeldarando

import com.ivieleague.disposable.CollectionItemDisposable
import com.ivieleague.disposable.ImmutableDisposableCollection
import com.ivieleague.disposable.MutableDisposableCollection
import com.ivieleague.event.PriorityListener1
import com.ivieleague.kotlin.ObservableCollection
import com.ivieleague.kotlin.ObservableCollectionWrapper
import com.lightningkite.kotlin.Disposable
import java.util.*

/**
 * MVC for games.
 * Created by josep on 1/10/2017.
 */
interface GameModel<in ControllerDependency, in ViewDependency>
    : Model<ControllerDependency, Disposable, ViewDependency, Disposable>

interface GameController {
    val services: MutableMap<String, Any>
    val step: TreeSet<PriorityListener1<Float>>
}

interface GameView {
    val services: MutableMap<String, Any>
    val render: TreeSet<PriorityListener1<Float>>
}


abstract class AbstractImmutableGameModelCollection<ControllerDependency, ViewDependency, T : GameModel<ControllerDependency, ViewDependency>>
    : Collection<T>, GameModel<ControllerDependency, ViewDependency> {

    abstract val collection: Collection<T>

    override val size: Int
        get() = collection.size

    override fun contains(element: T): Boolean = collection.contains(element)
    override fun containsAll(elements: Collection<T>): Boolean = collection.containsAll(elements)
    override fun isEmpty(): Boolean = collection.isEmpty()
    override fun iterator(): Iterator<T> = collection.iterator()

    override fun generateController(dependency: ControllerDependency): Disposable
            = ImmutableDisposableCollection(this.map { it.generateController(dependency) })

    override fun generateView(dependency: ViewDependency): Disposable
            = ImmutableDisposableCollection(this.map { it.generateView(dependency) })
}

class ImmutableGameModelCollection<ControllerDependency, ViewDependency>(
        collection: Collection<GameModel<ControllerDependency, ViewDependency>>
) : Collection<GameModel<ControllerDependency, ViewDependency>> by collection,
        GameModel<ControllerDependency, ViewDependency> {

    override fun generateController(dependency: ControllerDependency): Disposable
            = ImmutableDisposableCollection(this.map { it.generateController(dependency) })

    override fun generateView(dependency: ViewDependency): Disposable
            = ImmutableDisposableCollection(this.map { it.generateView(dependency) })
}

class MutableGameModelCollection<ControllerDependency, ViewDependency, T : GameModel<ControllerDependency, ViewDependency>>(
        collection: MutableCollection<T> = HashSet()
) : ObservableCollection<T> by ObservableCollectionWrapper<T>(collection),
        GameModel<ControllerDependency, ViewDependency> {

    override fun generateController(dependency: ControllerDependency): Disposable {
        val map = HashMap<T, Disposable>()
        val list = MutableDisposableCollection()
        for (item in this) {
            val gen = item.generateController(dependency)
            map[item] = gen
            list.add(gen)
        }
        val onAddListener = { item: T ->
            val gen = item.generateController(dependency)
            map[item] = gen
            list.add(gen)
            Unit
        }
        val onRemoveListener = { item: T ->
            val generated = map.remove(item)
            if (generated != null && list.remove(generated)) {
                generated.dispose()
            }
        }
        return ImmutableDisposableCollection(listOf(
                list,
                CollectionItemDisposable(onAdd, onAddListener),
                CollectionItemDisposable(onRemove, onRemoveListener)
        ))
    }

    override fun generateView(dependency: ViewDependency): Disposable {
        val map = HashMap<T, Disposable>()
        val list = MutableDisposableCollection()
        for (item in this) {
            val gen = item.generateView(dependency)
            map[item] = gen
            list.add(gen)
        }
        val onAddListener = { item: T ->
            val gen = item.generateView(dependency)
            map[item] = gen
            list.add(gen)
            Unit
        }
        val onRemoveListener = { item: T ->
            val generated = map.remove(item)
            if (generated != null && list.remove(generated)) {
                generated.dispose()
            }
        }
        return ImmutableDisposableCollection(listOf(
                list,
                CollectionItemDisposable(onAdd, onAddListener),
                CollectionItemDisposable(onRemove, onRemoveListener)
        ))
    }
}