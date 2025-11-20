package com.example.playlistmaker.ui.activity.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.Playlist

@Composable
fun PlaylistListItem(
    playlist: Playlist,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 13.dp)
            .padding(top = 5.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .padding(end = 8.dp)
            .size(45.dp)
            .clip(RoundedCornerShape(4.dp))
        if (!playlist.coverImageUri.isNullOrEmpty()) {
            AsyncImage(
                modifier = imageModifier,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(playlist.coverImageUri.toCoverModel())
                    .crossfade(true)
                    .build(),
                contentDescription = playlist.name,
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                modifier = imageModifier,
                painter = painterResource(id = R.drawable.add_photo_icon),
                contentDescription = playlist.name,
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(playlist.name, fontSize = 16.sp)
            val text = "${playlist.tracks.size} треков"
            Text(text, fontSize = 11.sp, color = Color(0xFFAEAFB4))
        }
    }
}