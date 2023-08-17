package ru.netology.nework.model

data class StateModel(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val error: Boolean = false,
    val loginError: Boolean = false,
    val registrationError: Boolean = false,
)

//data class LoginFormState(
//    val loginError: Boolean = false,
//    val registrationError: Boolean = false,
//)

