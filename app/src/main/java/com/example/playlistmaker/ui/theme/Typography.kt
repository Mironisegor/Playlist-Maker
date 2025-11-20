package com.example.playlistmaker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

object AppTypography {
    val YSD_Regular400 = FontFamily(
        Font(R.font.ysd_regular, FontWeight.Companion.W400)
    )
    val YSD_Medium500 = FontFamily(
        Font(R.font.yst_medium, FontWeight.Companion.W500)
    )
    val YSD_Medium400 = FontFamily(
        Font(R.font.yst_medium, FontWeight.Companion.W400)
    )

    val YSD_Bold700 = FontFamily(
        Font(R.font.yst_bold, FontWeight.Companion.W700)
    )
}

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

