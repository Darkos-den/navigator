package com.darkos.mvu.navigator

import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

abstract class NavGraphNavigator : Navigator {

    private var subNavigators: List<WeakReference<NavGraphNavigator>> = emptyList()
        get() {
            field = field.filter {
                it.get() != null
            }
            return field
        }
    val childs: List<NavGraphNavigator>
        get() = subNavigators.mapNotNull { it.get() }

    private var controller: WeakReference<NavController> = initController()
    private fun initController() = WeakReference<NavController>(null)

    fun attach(navigator: NavGraphNavigator) {
        subNavigators = subNavigators + WeakReference(navigator)
    }

    fun attachNavController(controller: NavController) {
        this.controller = WeakReference(controller)
    }

    override suspend fun navigate(navigation: Navigation): Boolean {
        return withContext(Dispatchers.Main) {
            controller.get()?.let {
                navigate(it, navigation)
            } ?: throw IllegalStateException(
                "controller not available"
            )
        }
    }

    abstract suspend fun navigate(controller: NavController, navigation: Navigation): Boolean
}