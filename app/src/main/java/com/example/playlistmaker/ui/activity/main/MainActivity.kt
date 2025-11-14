package com.example.playlistmaker.ui.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.playlistmaker.font.AppTypography
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.navigation.PlaylistHost
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistMakerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    PlaylistHost(navController = navController)
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPlaylists: () -> Unit,
    onNavigateToFavorites: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4169E1))
    ) {
        Text(
            text = stringResource(R.string.app_name),
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = 16.dp, top = 14.dp, bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(color = Color.White)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                SettingsMenuItem(
                    iconRes = R.drawable.search,
                    title = stringResource(R.string.search_title),
                    onClick = onNavigateToSearch
                )
                SettingsMenuItem(
                    iconRes = R.drawable.library,
                    title = stringResource(R.string.playlists),
                    onClick = onNavigateToPlaylists
                )
                SettingsMenuItem(
                    iconRes = R.drawable.favorite_border,
                    title = "Избранное",
                    onClick = onNavigateToFavorites
                )
                SettingsMenuItem(
                    iconRes = R.drawable.settings,
                    title = stringResource(R.string.settings_title),
                    onClick = onNavigateToSettings
                )
            }
        }
    }
}

@Composable
private fun SettingsMenuItem(iconRes: Int, title: String, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .padding(horizontal = 16.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color(0xFF212327),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            fontFamily = AppTypography.YSD_Medium500,
            fontSize = 22.sp,
            color = Color(0xFF212327),
            modifier = Modifier.weight(1f).padding(start = 16.dp)
        )
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MainScreenPreview() {
    PlaylistMakerTheme { 
        MainScreen(
            onNavigateToSearch = {},
            onNavigateToSettings = {},
            onNavigateToPlaylists = {},
            onNavigateToFavorites = {}
        ) 
    }
}