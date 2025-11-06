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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.AppTypography
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.Track

@Composable
fun TrackListItem(track: Track) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start=13.dp).height(61.dp).clickable {},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = "Трек ${track.trackName}",
            Modifier.size(45.dp).padding(end=8.dp)
        )
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