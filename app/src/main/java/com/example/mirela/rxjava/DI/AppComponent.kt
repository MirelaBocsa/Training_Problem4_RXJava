package com.example.mirela.rxjava.DI

import com.example.mirela.rxjava.SchoolRepository
import com.example.mirela.rxjava.ui.SchoolsFragment
import dagger.Component

@Component(modules = [RepositoryModule::class])
interface AppComponent {
    fun inject(schoolsFragment: SchoolsFragment)

    fun getSchoolRepository():SchoolRepository


}