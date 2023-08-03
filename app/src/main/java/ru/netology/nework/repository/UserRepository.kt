package ru.netology.nework.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.User
import ru.netology.nework.entity.UserEntity

interface UserRepository {

    val data: Flow<List<User>>

    suspend fun getAll()
    fun searchDatabase(searchQuery: String): Flow<List<UserEntity>>
}