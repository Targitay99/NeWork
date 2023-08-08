package ru.netology.nework.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import ru.netology.nework.dto.User
import ru.netology.nework.entity.UserEntity

interface UserRepository {

    val data: Flow<List<User>>

    suspend fun getAll()
    fun searchDatabase(searchQuery: String): Flow<List<UserEntity>>
    suspend fun getUserById(id: Long):User
}