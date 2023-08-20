package com.stevdza.san.mongodemo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.stevdza.san.mongodemo.R


val InterFont = FontFamily(
    Font(R.font.inter_medium, weight = FontWeight.Medium),
    Font(R.font.inter_bold, weight = FontWeight.Bold),
    Font(R.font.inter_regular, weight = FontWeight.Normal),
    Font(R.font.inter_semibold, weight = FontWeight.SemiBold)
)

val PasscodeKeyButtonStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp
)

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
val h1 = TextStyle(
    fontFamily = InterFont,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp
)
val body1 = TextStyle(
    fontFamily = InterFont,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
val button = TextStyle(
    fontFamily = InterFont,
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp
)
val subtitle1 = TextStyle(
    fontFamily = InterFont,
    fontWeight = FontWeight.Normal,
    fontSize = 10.sp
)

val h2 = TextStyle(
    fontFamily = InterFont,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp
)