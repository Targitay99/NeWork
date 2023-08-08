package ru.netology.nework.model

class UserModelState {

    data class UsersModelState(
        val loading: Boolean = false,
        val error: Boolean = false,
        val refreshing: Boolean = false,
    )
}