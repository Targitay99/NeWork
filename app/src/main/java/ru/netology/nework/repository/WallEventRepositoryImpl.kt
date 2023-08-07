package ru.netology.nework.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nework.api.EventApiService
import ru.netology.nework.api.WallApiService
import ru.netology.nework.dao.EventDao
import ru.netology.nework.dao.EventRemoteKeyDao
import ru.netology.nework.dao.PostDao
import ru.netology.nework.dao.PostRemoteKeyDao
import ru.netology.nework.db.AppDb
import ru.netology.nework.dto.Attachment
import ru.netology.nework.dto.AttachmentType
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.Media
import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.dto.Post
import ru.netology.nework.entity.EventEntity
import ru.netology.nework.entity.PostEntity
import ru.netology.nework.errors.ApiError
import ru.netology.nework.errors.AppError
import ru.netology.nework.errors.NetworkError
import java.io.IOException
import javax.inject.Inject

class WallEventRepositoryImpl @Inject constructor(

    private val eventDao: EventDao,
    private val eventApiService: EventApiService,
    private val appDb: AppDb,
    private val eventRemoteKeyDao: EventRemoteKeyDao,


) : WallEventRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun loadUserWallEvent(userId: Long): Flow<PagingData<Event>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        remoteMediator = WallEventRemoteMediator(
            eventApiService = eventApiService,
            eventDao = eventDao,
            eventRemoteKeyDao = eventRemoteKeyDao,
            appDb = appDb,
            authorId = userId,
        ),
        pagingSourceFactory = { eventDao.getPagingSource(userId) }
    ).flow
        .map {
            it.map(EventEntity::toDto)
        }
}