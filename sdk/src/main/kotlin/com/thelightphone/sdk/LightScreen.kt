package com.thelightphone.sdk

import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

abstract class SimpleLightScreen(sealedActivity: SealedLightActivity) : ViewModelStoreOwner {
    internal val activity = sealedActivity.activity

    override val viewModelStore: ViewModelStore = ViewModelStore()

    protected val dataStore: DataStore<Preferences>
        get() = activity.dataStore

    @Composable
    abstract fun Content()

    open val showBackBar: Boolean = true

    open fun willShow() {}
    open fun willHide() {}
    open fun onAppPause() {}

    internal open fun notifyWillShow() {
        willShow()
    }

    internal open fun notifyWillHide() {
        willHide()
    }

    internal open fun notifyAppPause() {
        onAppPause()
    }

    internal open fun destroy() {
        viewModelStore.clear()
    }

    fun navigateTo(screenFactory: (SealedLightActivity) -> SimpleLightScreen) {
        val screen = screenFactory(SealedLightActivity(activity))
        activity.navigateTo(screen)
    }

    open fun goBack() {
        activity.goBack()
    }
}

abstract class LightScreen<VM : LightViewModel>(
    sealedActivity: SealedLightActivity
) : SimpleLightScreen(sealedActivity) {
    abstract val viewModelClass: Class<VM>
    abstract fun createViewModel(): VM

    @Suppress("UNCHECKED_CAST")
    val viewModel: VM by lazy {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return createViewModel() as T
            }
        }
        ViewModelProvider(this, factory)[viewModelClass]
    }

    internal override fun notifyWillShow() {
        super.notifyWillShow()
        viewModel.onScreenShow(this)
    }

    internal override fun notifyWillHide() {
        super.notifyWillHide()
        viewModel.onScreenHide(this)
    }

    internal override fun notifyAppPause() {
        super.notifyAppPause()
        viewModel.onAppPause()
    }

    override fun goBack() {
        if (!viewModel.onBackPressed()) {
            super.goBack()
        }
    }
}
