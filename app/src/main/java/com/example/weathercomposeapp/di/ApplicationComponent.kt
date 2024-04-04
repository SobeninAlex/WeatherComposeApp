package com.example.weathercomposeapp.di

import android.content.Context
import com.example.weathercomposeapp.di.annotation.ApplicationScope
import com.example.weathercomposeapp.di.module.DataModule
import com.example.weathercomposeapp.di.module.PresentationModule
import com.example.weathercomposeapp.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        PresentationModule::class
    ]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }

}