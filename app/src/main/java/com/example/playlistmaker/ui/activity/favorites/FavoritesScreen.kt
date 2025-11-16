package com.example.playlistmaker.ui.activity.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.ui.theme.AppTypography
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.ui.activity.search.TrackListItem
import com.example.playlistmaker.ui.viewmodel.PlaylistViewModel

@Composable
fun FavoritesScreen(
    playlistViewModel: PlaylistViewModel,
    onNavigateToTrackDetails: (Track) -> Unit,
    onBack: () -> Unit
) {
    val favoriteTracks by playlistViewModel.favoriteList.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = null,
                tint = Color(0xFF1A1B22),
                modifier = Modifier
                    .size(16.dp)
                    .clickable(onClick = onBack)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = stringResource(R.string.favorites_title),
                fontFamily = AppTypography.YSD_Medium500,
                fontSize = 22.sp,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (favoriteTracks.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoriteTracks.size) { index ->
                    TrackListItem(
                        track = favoriteTracks[index],
                        onClick = {
                            onNavigateToTrackDetails(favoriteTracks[index])
                        }
                    )
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(154.dp))
                Image(
                    painter = painterResource(id = R.drawable.nothing_to_show),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                )
                Text(
                    text = stringResource(R.string.no_favorites_tracks),
                    color = Color(0xFF000000),
                    fontFamily = AppTypography.YSD_Medium400,
                    fontSize = 19.sp
                )
            }
        }
    }
}