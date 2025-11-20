package com.example.playlistmaker.ui.activity.playlist

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.playlistmaker.ui.theme.AppTypography
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.Playlist
import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.ui.activity.search.TrackListItem
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat

@SuppressLint("IntentReset")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    playlist: Playlist,
    navigateBack: () -> Unit,
    onNavigateToTrackDetails: (Track) -> Unit,
    onDeletePlaylist: () -> Unit,
    onUpdateCoverImage: (Long, String?) -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf(playlist.coverImageUri?.toCoverUri()) }
    var showCoverPermissionDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val requiredPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            val savedPath = uri?.let { saveCoverToInternalStorage(context, it) }
            if (savedPath != null) {
                selectedImageUri = savedPath.toCoverUri()
                onUpdateCoverImage(playlist.id, savedPath)
            }
        }
    }
    val openGallery = remember(galleryLauncher) {
        {
            val pickImageIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).apply {
                type = "image/*"
            }
            galleryLauncher.launch(pickImageIntent)
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            openGallery()
        }
    }
    val onCoverClick = remember {
        {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                requiredPermission
            ) == PackageManager.PERMISSION_GRANTED
            if (hasPermission) {
                openGallery()
            } else {
                showCoverPermissionDialog = true
            }
        }
    }
    LaunchedEffect(playlist.coverImageUri) {
        selectedImageUri = playlist.coverImageUri?.toCoverUri()
    }
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
        val playlistImageModifier = Modifier
            .padding(top = 4.dp)
            .padding(horizontal = 8.dp)
            .size(312.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCoverClick() }
        if (selectedImageUri != null) {
            AsyncImage(
                modifier = playlistImageModifier,
                model = ImageRequest.Builder(context)
                    .data(selectedImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
                Image(
                    modifier = Modifier
                        .padding(top = 120.dp)
                        .padding(bottom = 80.dp)
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onCoverClick() },
                    painter = painterResource(id = R.drawable.add_photo_icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
        }
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
                text = playlist.description.ifEmpty { "" },
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
                    .clickable(onClick = { showBottomSheet = true }),
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
                    text = playlist.name,
                    fontFamily = AppTypography.YSD_Medium500,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1B22),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "${playlist.tracks.size} треков",
                    fontFamily = AppTypography.YSD_Regular400,
                    fontSize = 16.sp,
                    color = Color(0xFFAEAFB4),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Поделиться",
                        fontFamily = AppTypography.YSD_Regular400,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1B22)
                    )
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Редактировать информацию",
                        fontFamily = AppTypography.YSD_Regular400,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1B22)
                    )
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                    showDeleteDialog = true
                                }
                            }
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Удалить плейлист",
                        fontFamily = AppTypography.YSD_Regular400,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1B22)
                    )
                }
            }
        }
    }

    if (showCoverPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showCoverPermissionDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.playlist_cover),
                    fontFamily = AppTypography.YSD_Medium500,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.permission_required),
                    fontFamily = AppTypography.YSD_Regular400,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCoverPermissionDialog = false
                        permissionLauncher.launch(requiredPermission)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.select_image),
                        fontFamily = AppTypography.YSD_Medium500
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showCoverPermissionDialog = false }) {
                    Text(
                        text = stringResource(android.R.string.cancel),
                        fontFamily = AppTypography.YSD_Regular400
                    )
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Хотите удалить плейлист \"${playlist.name}\"?",
                    fontFamily = AppTypography.YSD_Regular400,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1B22)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeletePlaylist()
                        navigateBack()
                    }
                ) {
                    Text(
                        text = "ДА",
                        fontFamily = AppTypography.YSD_Regular400,
                        fontSize = 16.sp,
                        color = Color(0xFFEC5757)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(
                        text = "НЕТ",
                        fontFamily = AppTypography.YSD_Regular400,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1B22)
                    )
                }
            }
        )
    }
}