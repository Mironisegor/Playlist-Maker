package com.example.playlistmaker.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    trackWidth: Dp = 32.dp,
    trackHeight: Dp = 12.dp,
    thumbSize: Dp = 18.dp,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedColor: Color = Color(0xFFE6E8EB)
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (isChecked) trackWidth - thumbSize else 0.dp,
        label = "switch_animation"
    )

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(thumbSize)
            .clickable { onCheckedChange(!isChecked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .width(trackWidth)
                .height(trackHeight)
                .clip(RoundedCornerShape(percent = 50))
                .background(
                    color = if (isChecked) checkedColor.copy(alpha = 0.5f) else uncheckedColor
                )
                .align(Alignment.CenterStart)
        )
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = thumbOffset)
                .clip(CircleShape)
                .background(
                    color = if (isChecked) checkedColor else Color(0xFFAEAFB4)
                )
                .border(
                    width = 1.dp,
                    color = if (isChecked) checkedColor else Color(0xFFBDBDBD),
                    shape = CircleShape
                )
        )
    }
}

