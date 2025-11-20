package com.example.playlistmaker.ui.activity.playlist

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.playlistmaker.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import androidx.core.content.ContextCompat
import com.example.playlistmaker.ui.theme.AppTypography
import com.example.playlistmaker.ui.viewmodel.NewPlaylistViewModel

@SuppressLint("IntentReset")
@Composable
fun CreatePlaylistScreen(
    onBack: () -> Unit,
    newPlaylistViewModel: NewPlaylistViewModel
) {
    var nameText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var isNameFocused by remember { mutableStateOf(false) }
    var isDescriptionFocused by remember { mutableStateOf(false) }
    val isCreateEnabled = nameText.isNotEmpty()
    val context = LocalContext.current
    val requiredPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    val coverImageUri by newPlaylistViewModel.coverImageUri.collectAsState()
    val selectedImageUri = coverImageUri?.toCoverUri()
    var showPermissionDialog by remember { mutableStateOf(false) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            val savedPath = uri?.let { saveCoverToInternalStorage(context, it) }
            newPlaylistViewModel.setCoverImageUri(savedPath)
        }
    }
    val openGallery = remember(galleryLauncher) {
        {
            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).apply { type = "image/*" }
            galleryLauncher.launch(pickIntent)
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
                showPermissionDialog = true
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
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = stringResource(R.string.new_playlist),
                fontFamily = AppTypography.YSD_Medium500,
                fontSize = 22.sp,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(136.dp))

        val coverModifier = Modifier
            .size(180.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF4F3F7))
            .clickable { onCoverClick() }
        if (selectedImageUri != null) {
            AsyncImage(
                modifier = coverModifier,
                model = ImageRequest.Builder(context)
                    .data(selectedImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = coverModifier,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_photo_icon),
                    contentDescription = null,
                    tint = Color(0xFFAEAFB4),
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(150.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nameText,
                onValueChange = { nameText = it },
                label = {
                    Text(
                        "Название*",
                        color = if (isNameFocused) Color(0xFF3772E7) else Color(0xFF1A1B22)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isNameFocused = focusState.isFocused
                    },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color(0xFF1A1B22),
                    unfocusedTextColor = Color(0xFF1A1B22),
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent,
                    cursorColor = Color(0xFF3772E7),
                    focusedIndicatorColor = Color(0xFF3772E7),
                    unfocusedIndicatorColor = Color(0xFFAEAFB4)
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF1A1B22),
                    fontFamily = AppTypography.YSD_Regular400
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                label = {
                    Text(
                        "Описание",
                        color = if (isDescriptionFocused) Color(0xFF3772E7) else Color(0xFF1A1B22)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isDescriptionFocused = focusState.isFocused
                    },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color(0xFF1A1B22),
                    unfocusedTextColor = Color(0xFF1A1B22),
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent,
                    cursorColor = Color(0xFF3772E7),
                    focusedIndicatorColor = Color(0xFF3772E7),
                    unfocusedIndicatorColor = Color(0xFFAEAFB4)
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF1A1B22),
                    fontFamily = AppTypography.YSD_Regular400
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 17.dp)
                .padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        color = if (isCreateEnabled) Color(0xFF3772E7) else Color(0xFFAEAFB4),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        enabled = isCreateEnabled,
                        onClick = {
                            newPlaylistViewModel.createNewPlaylist(nameText, descriptionText)
                            onBack()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "СОЗДАТЬ",
                    fontFamily = AppTypography.YSD_Medium500,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
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
                        showPermissionDialog = false
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
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text(
                        text = stringResource(android.R.string.cancel),
                        fontFamily = AppTypography.YSD_Regular400
                    )
                }
            }
        )
    }
}