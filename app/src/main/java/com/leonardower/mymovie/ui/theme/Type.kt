package com.leonardower.mymovie.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.leonardower.mymovie.R

val AkzidenzGroteskFontFamily = FontFamily(
    Font(R.font.akzidenzgroteskpro_bold, FontWeight.Bold),
    Font(R.font.akzidenzgroteskpro_md, FontWeight.Medium),
    Font(R.font.akzidenzgroteskpro_regular, FontWeight.Normal),
    Font(R.font.akzidenzgroteskpro_light, FontWeight.Light),
)

val AccentFontFamily = FontFamily(
    Font(R.font.belarus, FontWeight.Normal),
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = AccentFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = AkzidenzGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = AkzidenzGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = AkzidenzGroteskFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = AkzidenzGroteskFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = AkzidenzGroteskFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = AkzidenzGroteskFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = AkzidenzGroteskFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 11.sp,
    ),
)