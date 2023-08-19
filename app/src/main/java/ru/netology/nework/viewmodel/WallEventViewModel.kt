package ru.netology.nework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.repository.WallEventRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WallEventViewModel@Inject constructor(
    private val eventRepository: WallEventRepository,
    private val appAuth: AppAuth,
) : ViewModel() {

    fun loadUserWallEvent(id: Long) = appAuth.authStateFlow
        .flatMapLatest { (myId, _) ->
            eventRepository.loadUserWallEvent(id)
                .map { pagingData ->
                pagingData.map { event ->
                    event.copy(
                        ownedByMe = event.authorId == myId,
                        likedByMe = event.likeOwnerIds.contains(myId)
                    )
                }
            }
        }
}