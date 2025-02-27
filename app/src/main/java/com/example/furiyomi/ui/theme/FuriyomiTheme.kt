package com.example.furiyomi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val darkColorScheme = AppColorScheme(
    background = Black100,
    onBackground = White80,
    primary = Blue30,
    onPrimary = White80,
    secondary = Black80,
    onSecondary = White60
)

val lightColorScheme = AppColorScheme(
    background = White100,
    onBackground = Black70,
    primary = Blue100,
    onPrimary = Black80,
    secondary = Gray20,
    onSecondary = Black60
)

private val typography = AppTypography(
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleNormal = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    body = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    labelNormal = TextStyle(
        fontFamily = Inter,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontSize = 10.sp
    )
)

private val size = AppSize(
    large = 24.dp,
    medium = 16.dp,
    normal = 12.dp,
    small = 8.dp
)

@Composable
fun FuriyomiTheme(
    colorScheme: AppColorScheme,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme
    val rippleIndication = rememberRipple()

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
//        LocalAppShape provides shape,
        LocalAppSize provides size,
        LocalRippleTheme provides FullAlphaRipple,
        content = content
    )

}

object FuriyomiTheme {

    val colorScheme: AppColorScheme
        @Composable get() = LocalAppColorScheme.current

    val typography: AppTypography
        @Composable get() = LocalAppTypography.current
    /*
        val shape: AppShape
            @Composable get() = LocalAppShape.current*/

    val size: AppSize
        @Composable get() = LocalAppSize.current

}