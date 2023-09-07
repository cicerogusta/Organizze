package com.example.organizze.di

import com.example.organizze.data.repository.FirebaseRepository
import com.example.organizze.data.repository.FirebaseRepositoryImp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase,
    ): FirebaseRepository {
        return FirebaseRepositoryImp(auth, database)
    }
}