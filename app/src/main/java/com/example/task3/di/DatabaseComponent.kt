package com.example.task3.di

import com.example.task3.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule::class])
interface DatabaseComponent {
    fun inject(mainActivity: MainActivity)
}