package com.example.playlistmaker.ui.activity.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.font.AppTypography
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.Playlist
import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.ui.activity.search.TrackListItem

@Composable
fun PlaylistScreen(
    playlist: Playlist,
    navigateBack: () -> Unit,
    onNavigateToTrackDetails: (Track) -> Unit
) {
    val totalMinutes = playlist.tracks.sumOf { track ->
        val timeParts = track.trackTime.split(":")
        if (timeParts.size == 2) {
            timeParts[0].toIntOrNull() ?: 0
        } else {
            0
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = navigateBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = null,
                    tint = Color(0xFF1A1B22),
                    modifier = Modifier
                        .size(16.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Image(
            modifier = Modifier
                .size(312.dp)
                .padding(top=4.dp)
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(8.dp)),
            painter = painterResource(id = R.drawable.add_photo_icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Gray)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .padding(bottom = 8.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(bottom = 8.dp),
                text = playlist.name,
                fontFamily = AppTypography.YSD_Bold700,
                fontSize = 24.sp,
                color = Color(0xFF1A1B22)
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(bottom = 8.dp),
                text = "2022",
                fontFamily = AppTypography.YSD_Regular400,
                fontSize = 18.sp,
                color = Color(0xFF1A1B22)
            )
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 = "$totalMinutes минут"
                Text(
                    modifier = Modifier
                        .padding(end = 5.dp),
                    text = text1,
                    fontFamily = AppTypography.YSD_Regular400,
                    fontSize = 18.sp,
                    color = Color(0xFF1A1B22)
                )
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(
                            color = Color(color = 0xFF1A1B22),
                            shape = CircleShape
                        )
                )
                val text2 = "${playlist.tracks.size} треков"
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp),
                    text = text2,
                    fontFamily = AppTypography.YSD_Regular400,
                    fontSize = 18.sp,
                    color = Color(0xFF1A1B22)
                )
            }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = {}),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.three_points_icon),
                    contentDescription = null,
                    tint = Color(0xFF1A1B22),
                    modifier = Modifier
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(playlist.tracks.size) { index ->
                TrackListItem(
                    track = playlist.tracks[index],
                    onClick = {
                        onNavigateToTrackDetails(playlist.tracks[index])
                    }
                )
            }
        }
    }
}