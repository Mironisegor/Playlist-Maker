package com.example.playlistmaker.ui.activity.track

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.playlistmaker.ui.theme.AppTypography
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.ui.viewmodel.PlaylistViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    track: Track,
    onBack: () -> Unit,
    playlistViewModel: PlaylistViewModel
) {
    var currentTrack by remember { mutableStateOf(track) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val playlists by playlistViewModel.playlists.collectAsState(initial = emptyList())
    val storedTrack by playlistViewModel.isExist(track).collectAsState(initial = null)

    LaunchedEffect(storedTrack) {
        storedTrack?.let { currentTrack = it }
    }
    
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_playlist),
                    fontFamily = AppTypography.YSD_Medium500,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1B22),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                if (playlists.isEmpty()) {
                    Text(
                        text = "Нет плейлистов",
                        color = Color(0xFFAEAFB4),
                        fontFamily = AppTypography.YSD_Regular400,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn {
                        items(playlists) { playlist ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        // Используем storedTrack если он доступен, иначе currentTrack
                                        val trackToAdd = storedTrack ?: currentTrack
                                        val updatedTrack = trackToAdd.copy(playlistId = playlist.id)
                                        currentTrack = updatedTrack
                                        playlistViewModel.insertSongToPlaylist(updatedTrack, playlist.id)
                                        scope.launch {
                                            sheetState.hide()
                                        }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                    }
                                    .padding(horizontal = 13.dp)
                                    .padding(bottom = 8.dp)
                                    .padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_photo_icon),
                                    contentDescription = null,
                                    tint = Color(0xFF1A1B22),
                                    modifier = Modifier
                                        .size(45.dp)
                                        .padding(end = 8.dp)
                                )
                                Column {
                                    Text(
                                        text = playlist.name,
                                        fontFamily = AppTypography.YSD_Regular400,
                                        fontSize = 16.sp,
                                        color = Color(0xFF000000)
                                    )
                                    val amountOfTracks = "${playlist.tracks.size} треков"
                                    Text(
                                        text = amountOfTracks,
                                        fontFamily = AppTypography.YSD_Regular400,
                                        fontSize = 11.sp,
                                        color = Color(0xFFAEAFB4),
                                        modifier = Modifier.padding(top = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
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
                    .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = null,
                    tint = Color(0xFF1A1B22),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onBack)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (track.artworkUrl != null) {
                val highQualityUrl = track.artworkUrl.replace("100x100", "600x600")
                AsyncImage(
                    model = highQualityUrl,
                    contentDescription = "Обложка трека ${track.trackName}",
                    modifier = Modifier
                        .padding(top = 26.dp, bottom = 24.dp)
                        .size(312.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.add_photo_icon),
                    error = painterResource(id = R.drawable.add_photo_icon)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_music),
                    contentDescription = "Трек ${track.trackName}",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(
                text = currentTrack.trackName,
                color = Color(0xFF1A1B22),
                fontFamily = AppTypography.YSD_Medium400,
                fontSize = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            Text(
                text = currentTrack.artistName,
                color = Color(0xFF1A1B22),
                fontFamily = AppTypography.YSD_Medium400,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 54.dp)
            )
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(bottom = 24.dp),
               horizontalArrangement = Arrangement.SpaceBetween
           ) {
               FloatingActionButton(
                   onClick = { showBottomSheet = true },
                   containerColor = Color.LightGray,
                   contentColor = Color.White,
                   shape = CircleShape
               ) {
                   Icon(
                       painter = painterResource(id = R.drawable.add_to_playlist_icon),
                       modifier = Modifier
                           .size(24.dp),
                       tint = Color.White,
                       contentDescription = stringResource(R.string.add_playlist)
                   )
               }
               FloatingActionButton(
                   onClick = {
                       val newFavorite = !currentTrack.favorite
                       currentTrack = currentTrack.copy(favorite = newFavorite)
                       playlistViewModel.toggleFavorite(currentTrack, newFavorite)
                   },
                   containerColor = if (currentTrack.favorite) Color.Red else Color.LightGray,
                   contentColor = Color.White,
                   shape = CircleShape
               ) {
                   Icon(
                       painter = painterResource(id = R.drawable.add_to_favorite_icon),
                       modifier = Modifier
                           .size(24.dp),
                       tint = Color.White,
                       contentDescription = stringResource(R.string.add_to_favorite)
                   )
               }
           }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.duration),
                    color = Color(0xFFAEAFB4),
                    fontFamily = AppTypography.YSD_Regular400,
                    fontSize = 13.sp
                )
                Text(
                    text = currentTrack.trackTime,
                    color = Color(0xFF1A1B22),
                    fontFamily = AppTypography.YSD_Regular400,
                    fontSize = 13.sp
                )
            }
        }
    }
}