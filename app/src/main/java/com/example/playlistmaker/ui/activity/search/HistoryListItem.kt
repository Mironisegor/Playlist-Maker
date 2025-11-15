package com.example.playlistmaker.ui.activity.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.font.AppTypography

@Composable
fun HistoryListItem(
    historyRequest: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.clock_icon),
            contentDescription = null,
            tint = Color(0xFFAEAFB4),
            modifier = Modifier
                .padding(start = 14.dp)
                .size(24.dp)
        )
        Text(historyRequest,
            color = Color(color = 0xFF1A1B22),
            fontFamily = AppTypography.YSD_Regular400,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 6.dp)
        )
    }
}