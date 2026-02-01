package com.leonardower.mymovie.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.leonardower.mymovie.R

val OpenSansFontFamily = FontFamily(
    Font(R.font.os_extra_bold, FontWeight.ExtraBold),
    Font(R.font.os_bold, FontWeight.Bold),
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp,
    ),
    displayLarge = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = OpenSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
    ),
)