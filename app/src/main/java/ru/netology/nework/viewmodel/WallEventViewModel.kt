package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.dto.AttachmentType
import ru.netology.nework.dto.Coordinates
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.EventType
import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.model.MediaModel
import ru.netology.nework.model.StateModel
import ru.netology.nework.repository.WallEventRepository
import ru.netology.nework.utils.SingleLiveEvent
import java.io.InputStream
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