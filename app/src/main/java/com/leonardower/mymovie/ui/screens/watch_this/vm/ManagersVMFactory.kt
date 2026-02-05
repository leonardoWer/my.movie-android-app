package com.leonardower.mymovie.ui.screens.watch_this.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leonardower.mymovie.App
import com.leonardower.mymovie.ui.screens.home.vm.HomeVM

object ManagersVMFactory : ViewModelProvider.Factory {
    private val app = App.instance

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            HomeVM::class.java -> HomeVM(
                filmManager = app.appModule.filmManager,
                genreManager = app.appModule.genreManager
            ) as T

            WatchThisVM::class.java -> WatchThisVM(
                filmManager = app.appModule.filmManager,
                genreManager = app.appModule.genreManager
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}