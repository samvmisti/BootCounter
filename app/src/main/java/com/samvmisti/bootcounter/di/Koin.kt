package com.samvmisti.bootcounter.di

import androidx.room.Room
import com.samvmisti.bootcounter.App
import com.samvmisti.bootcounter.data.MainDatabase
import com.samvmisti.bootcounter.ui.theme.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

private val appModule = module {
    single {
        Room.databaseBuilder(androidContext(), MainDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }
    viewModel { MainViewModel(get()) }
}

fun startDI(myApplication: App) {
    startKoin {
        val modules = listOf(appModule)
        androidContext(myApplication)
            .printLogger()
            .modules(modules)
    }
}