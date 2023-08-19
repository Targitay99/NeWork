package ru.netology.nework.model

data class StateModel(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val error: Boolean = false,
)

data class LoginStateModel(
    val loginError: Boolean = false,
    val registrationError: Boolean = false,
)

