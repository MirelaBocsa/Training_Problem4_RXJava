package com.example.mirela.rxjava.DI

import com.example.mirela.rxjava.SchoolRepository
import dagger.Module
import dagger.Provides


@Module
class RepositoryModule{

    @Provides
    fun provideSchoolsRepository(): SchoolRepository {
        return SchoolRepository()
    }
}