package ru.netology.nework.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nework.api.UserApiService
import ru.netology.nework.dto.User
import ru.netology.nework.entity.UserEntity
import ru.netology.nework.model.StateModel
import ru.netology.nework.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userApiService: UserApiService,
) : ViewModel() {

    val data: LiveData<List<User>> =
        userRepository.data
            .asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<StateModel>()
    val dataState: LiveData<StateModel>
        get() = _dataState

    private val _user = MutableLiveData<User>()
    val user: MutableLiveData<User>
        get() = _user

    private val _userIds = MutableLiveData<Set<Long>>()
    val userIds: LiveData<Set<Long>>
        get() = _userIds

    init {
        getUsers()
    }

    private fun getUsers() = viewModelScope.launch {
        _dataState.postValue(StateModel(loading = true))
        try {
            userRepository.getAll()
            _dataState.postValue(StateModel())
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true)
        }
    }

fun getUserById(id: Long) = viewModelScope.launch {
    try {
        _dataState.value = StateModel(loading = true)
        user.value = userRepository.getUserById(id)
        _dataState.value = StateModel()
    } catch (e: Exception) {
        _dataState.value = StateModel(error = true)
    }
}



    fun getUsersIds(set: Set<Long>) =
        viewModelScope.launch {
            _userIds.value = set
        }
    fun searchDatabase(searchQuery: String): LiveData<List<UserEntity>> {
        return userRepository.searchDatabase(searchQuery).asLiveData()
    }


}