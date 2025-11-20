package com.example.playlistmaker.ui.activity.playlist

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.Playlist
import com.example.playlistmaker.ui.theme.AppTypography
import com.example.playlistmaker.ui.viewmodel.PlaylistViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsScreen(
    modifier: Modifier,
    playlistViewModel: PlaylistViewModel,
    addNewPlaylist: () -> Unit,
    navigateToPlaylist: (Long) -> Unit,
    navigateBack: () -> Unit
) {
    val playlists by playlistViewModel.playlists.collectAsState(emptyList())
    var showMergeSheet by remember { mutableStateOf(false) }
    var selectedPlaylist by remember { mutableStateOf<Playlist?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val hideSheet: () -> Unit = {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            showMergeSheet = false
            selectedPlaylist = null
        }
    }

    if (showMergeSheet && selectedPlaylist != null) {
        val currentPlaylist = selectedPlaylist!!
        val otherPlaylists = playlists.filter { it.id != currentPlaylist.id }
        ModalBottomSheet(
            onDismissRequest = hideSheet,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.merge_playlists_title),
                    fontFamily = AppTypography.YSD_Medium500,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1B22),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                if (otherPlaylists.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_other_playlists),
                        color = Color(0xFFAEAFB4),
                        fontFamily = AppTypography.YSD_Regular400,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn {
                        items(otherPlaylists) { playlist ->
                            val imageModifier = Modifier
                                .padding(end = 8.dp)
                                .size(45.dp)
                                .clip(RoundedCornerShape(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        playlistViewModel.mergePlaylists(currentPlaylist.id, playlist.id)
                                        Toast
                                            .makeText(
                                                context,
                                                context.getString(
                                                    R.string.playlist_merge_message,
                                                    currentPlaylist.name,
                                                    playlist.name
                                                ),
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                        hideSheet()
                                    }
                                    .padding(horizontal = 13.dp)
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (!playlist.coverImageUri.isNullOrEmpty()) {
                                    AsyncImage(
                                        modifier = imageModifier,
                                        model = ImageRequest.Builder(context)
                                            .data(playlist.coverImageUri.toCoverModel())
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = playlist.name,
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.add_photo_icon),
                                        contentDescription = null,
                                        tint = Color(0xFF1A1B22),
                                        modifier = imageModifier
                                    )
                                }
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

    Box(modifier = Modifier.fillMaxSize()) {
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
                            .clickable(onClick = navigateBack)
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = stringResource(R.string.playlists),
                    fontFamily = AppTypography.YSD_Medium500,
                    fontSize = 22.sp,
                    color = Color(0xFF1A1B22)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            ) {
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(playlists.size) { index ->
                        PlaylistListItem(
                            playlist = playlists[index],
                            onClick = {
                                navigateToPlaylist(playlists[index].id)
                            },
                            onLongClick = {
                                if (playlists.size > 1) {
                                    selectedPlaylist = playlists[index]
                                    showMergeSheet = true
                                }
                            }
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 31.dp)
                .padding(end = 17.dp)
                .align(Alignment.BottomEnd),
            onClick = { addNewPlaylist() },
            containerColor = Color.LightGray,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus_icon),
                modifier = Modifier
                    .size(24.dp),
                tint = Color.White,
                contentDescription = stringResource(R.string.add_playlist)
            )
        }
    }
}
