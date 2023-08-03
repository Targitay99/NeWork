package ru.netology.nework.utils

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import ru.netology.nework.AppActivity

class MediaHelper(view: StyledPlayerView, url: String) {
    val playerView = view
    val player = ExoPlayer.Builder(MAIN).build()
    val media = url

    fun create() {
        playerView.player = player
        val mediaItem = MediaItem.fromUri(media)
        player.setMediaItem(mediaItem)
        player.prepare()
    }
    fun onPlay() {
        player.play()
    }
}