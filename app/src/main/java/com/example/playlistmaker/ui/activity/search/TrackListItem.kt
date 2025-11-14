package com.example.playlistmaker.ui.activity.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.font.AppTypography
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.Track
import coil3.compose.AsyncImage

@Composable
fun TrackListItem(
    track: Track,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start=13.dp)
            .height(61.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            if (track.artworkUrl != null) {
                AsyncImage(
                    model = track.artworkUrl,
                    contentDescription = "Обложка трека ${track.trackName}",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_music),
                    error = painterResource(id = R.drawable.ic_music)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_music),
                    contentDescription = "Трек ${track.trackName}",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = track.trackName,
                color = Color.Black,
                fontFamily = AppTypography.YSD_Regular400,
                fontSize = 22.sp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    track.artistName,
                    color = Color(color = 0xFFAEAFB4),
                    fontFamily = AppTypography.YSD_Regular400,
                    fontSize = 17.sp
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(
                            color = Color(color = 0xFFAEAFB4),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(track.trackTime,
                    color = Color(color = 0xFFAEAFB4),
                    fontFamily = AppTypography.YSD_Regular400,
                    fontSize = 17.sp
                )
            }
        }
        Column(
            modifier = Modifier.padding(end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = null,
                tint = Color(0xFFB0B6BE),
                modifier = Modifier
                    .width(8.dp)
                    .height(14.dp)
            )
        }
    }
}