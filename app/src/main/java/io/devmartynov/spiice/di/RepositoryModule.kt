package io.devmartynov.spiice.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.devmartynov.spiice.repository.note.NotesRepository
import io.devmartynov.spiice.repository.note.NotesRepositoryImpl
import io.devmartynov.spiice.repository.user.UserPreferencesRepository
import io.devmartynov.spiice.repository.user.UserPreferencesRepositoryImpl
import io.devmartynov.spiice.repository.user.UserRepository
import io.devmartynov.spiice.repository.user.UserRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindNotesRepository(impl: NotesRepositoryImpl): NotesRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository
}