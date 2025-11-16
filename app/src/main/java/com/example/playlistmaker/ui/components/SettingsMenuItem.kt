package com.example.playlistmaker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.ui.theme.AppTypography

@Composable
fun SettingsMenuItem(
    iconRes: Int?,
    title: String,
    onClick: (() -> Unit)? = null,
    isDark: MutableState<Boolean>? = null,
    iconWidth: Dp = 24.dp,
    iconHeight: Dp = 24.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp)
            .padding(start = 16.dp)
            .height(61.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontFamily = AppTypography.YSD_Regular400,
            fontSize = 16.sp,
            color = Color.Black,
        )

        if (iconRes == null && isDark != null) {
            CustomSwitch(
                isChecked = isDark.value,
                onCheckedChange = { newValue ->
                    isDark.value = newValue
                }
            )
        } else if (iconRes != null) {
            Box(Modifier
                .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = Color(0xFFAEAFB4),
                    modifier = Modifier
                        .width(iconWidth)
                        .height(iconHeight)
                )
            }
        }
    }
}

