package com.example.allmovies.di

import android.content.Context
import com.example.allmovies.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
//        DatabaseModule::class,
        NetworkModule::class,
        DispatcherModule::class,
        DataModule::class,
        ViewModelBuilderModule::class,
        SubcomponentsModule::class,
        MainModule::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    fun mainComponent(): MainComponent.Factory
}

//@Subcomponent(modules = )
//interface YourActivitySubcomponent : AndroidInjector<MainActivity> {
//    @Subcomponent.Factory
//    interface Factory : AndroidInjector.Factory<MainActivity> {}
//}

@Module(subcomponents = [MainComponent::class])
object SubcomponentsModule
