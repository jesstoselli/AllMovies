package com.example.allmovies.di

import androidx.fragment.app.Fragment
import com.example.allmovies.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [])
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: Fragment)
}
