package ru.netology.nework.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nework.repository.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindsPostRepository(impl: PostRepositoryImpl): PostRepository

    @Singleton
    @Binds
    fun bindsEventRepository(impl: EventRepositoryImpl): EventRepository

    @Singleton
    @Binds
    fun bindsWallEventRepository(impl: WallEventRepositoryImpl): WallEventRepository

    @Singleton
    @Binds
    fun bindsWallRepository(impl: WallRepositoryImpl): WallRepository

    @Singleton
    @Binds
    fun bindsUserRepository(impl: UserRepositoryImpl): UserRepository

   @Singleton
   @Binds
   fun bindsJobRepository(impl: JobRepositoryImpl): JobRepository

//    @Singleton
//    @Binds
//    fun bindsSignInRepository(impl: SignInRepositoryImpl): SignInRepository
//
//    @Singleton
//    @Binds
//    fun bindsSignUpRepository(impl: SignUpRepositoryImpl): SignUpRepository
}