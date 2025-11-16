package com.example.playlistmaker.ui.activity.playlist

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import com.example.playlistmaker.ui.viewmodel.PlaylistViewModel

@Composable
fun PlaylistsScreen(
    modifier: Modifier,
    playlistViewModel: PlaylistViewModel,
    addNewPlaylist: () -> Unit,
    navigateToPlaylist: (Long) -> Unit,
    navigateBack: () -> Unit
) {
    val playlists by playlistViewModel.playlists.collectAsState(emptyList())


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
                        PlaylistListItem(playlist = playlists[index], onClick = { 
                            navigateToPlaylist(playlists[index].id)
                        })
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
