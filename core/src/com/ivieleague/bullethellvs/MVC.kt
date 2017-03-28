package com.ivieleague.bullethellvs

/**
 * Model/View/Controller
 * Created by josep on 1/10/2017.
 */
interface GeneratesController<in Dependency, out Controller> {
    fun generateController(dependency: Dependency): Controller
}

interface GeneratesView<in Dependency, out View> {
    fun generateView(dependency: Dependency): View
}

interface Model<in ControllerDependency, out Controller, in ViewDependency, out View>
    : GeneratesController<ControllerDependency, Controller>, GeneratesView<ViewDependency, View>