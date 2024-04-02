package com.example.weathercomposeapp.di

import android.content.Context
import com.example.weathercomposeapp.di.annotation.ApplicationScope
import com.example.weathercomposeapp.di.module.DataModule
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }

}