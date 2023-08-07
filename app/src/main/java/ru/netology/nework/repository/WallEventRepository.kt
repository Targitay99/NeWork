package ru.netology.nework.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.Event


interface WallEventRepository {

    fun loadUserWallEvent(userId: Long): Flow<PagingData<Event>>
}