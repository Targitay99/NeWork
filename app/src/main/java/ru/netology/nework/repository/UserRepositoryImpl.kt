package ru.netology.nework.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.nework.api.UserApiService
import ru.netology.nework.dao.UserDao
import ru.netology.nework.dto.User
import ru.netology.nework.entity.UserEntity
import ru.netology.nework.entity.toDto
import ru.netology.nework.entity.toUserEntity
import ru.netology.nework.errors.ApiError
import ru.netology.nework.errors.NetworkError
import ru.netology.nework.module.checkResponse
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userApiService: UserApiService,
) : UserRepository {

    override val data: Flow<List<User>> =
        userDao.getAll()
            .map { it.toDto() }
            .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            userDao.getAll()
            val response = userApiService.getUsers()
            if (!response.isSuccessful) {
                throw ApiError(response.message())
            }
            val body = response.body() ?: throw ApiError(response.message())
            userDao.insert(body.toUserEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError()
        }
    }

    override suspend fun getUserById(id: Long) : User{
        try {
            val response = userApiService.getUserById(id)
            checkResponse(response)
            return response.body() ?: throw Exception()
        }catch(e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw ru.netology.nework.errors.UnknownError
        }
    }

    override fun searchDatabase(searchQuery: String): Flow<List<UserEntity>> {
        return userDao.searchDatabase(searchQuery)
    }
}