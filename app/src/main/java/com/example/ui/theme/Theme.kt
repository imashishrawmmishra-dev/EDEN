package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val LocalIsBlackAndWhite = staticCompositionLocalOf { false }

private val BlackAndWhiteLightColorScheme =
  lightColorScheme(
    primary = Color(0xFF111111),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE8E8E8),
    onPrimaryContainer = Color(0xFF111111),
    secondary = Color(0xFF333333),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEEEEEE),
    onSecondaryContainer = Color(0xFF222222),
    tertiary = Color(0xFF555555),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFDDDDDD),
    onTertiaryContainer = Color(0xFF222222),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF111111),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111111),
    surfaceVariant = Color(0xFFF4F4F4),
    onSurfaceVariant = Color(0xFF333333),
    outline = Color(0xFF777777),
    outlineVariant = Color(0xFFCCCCCC)
  )

private val BlackAndWhiteDarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF282828),
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFFCCCCCC),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF3A3A3A),
    onSecondaryContainer = Color(0xFFEEEEEE),
    tertiary = Color(0xFFAAAAAA),
    onTertiary = Color(0xFF000000),
    background = Color(0xFF121212),
    onBackground = Color(0xFFEEEEEE),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFEEEEEE),
    surfaceVariant = Color(0xFF262626),
    onSurfaceVariant = Color(0xFFCCCCCC),
    outline = Color(0xFF888888),
    outlineVariant = Color(0xFF444444)
  )

private val DarkColorScheme =
  darkColorScheme(
    primary = EdenEmeraldDark,
    onPrimary = EdenOnEmeraldDark,
    primaryContainer = EdenEmeraldContainerDark,
    onPrimaryContainer = EdenOnEmeraldContainerDark,
    secondary = EdenTealDark,
    onSecondary = EdenOnTealDark,
    secondaryContainer = EdenTealContainerDark,
    background = EdenBackgroundDark,
    surface = EdenSurfaceDark,
    surfaceVariant = EdenSurfaceVariantDark,
    outline = EdenOutlineDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EdenEmerald,
    onPrimary = EdenOnEmerald,
    primaryContainer = EdenEmeraldContainer,
    onPrimaryContainer = EdenOnEmeraldContainer,
    secondary = EdenTeal,
    onSecondary = EdenOnTeal,
    secondaryContainer = EdenTealContainer,
    onSecondaryContainer = EdenOnTealContainer,
    tertiary = EdenAmber,
    onTertiary = EdenOnAmber,
    tertiaryContainer = EdenAmberContainer,
    onTertiaryContainer = EdenOnAmberContainer,
    background = EdenBackgroundLight,
    surface = EdenSurfaceLight,
    surfaceVariant = EdenSurfaceVariantLight,
    outline = EdenOutlineLight
  )

@Composable
fun EDENTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve brand environmental colors
  isBlackAndWhite: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      isBlackAndWhite && darkTheme -> BlackAndWhiteDarkColorScheme
      isBlackAndWhite -> BlackAndWhiteLightColorScheme
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  CompositionLocalProvider(LocalIsBlackAndWhite provides isBlackAndWhite) {
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
  }
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  isBlackAndWhite: Boolean = false,
  content: @Composable () -> Unit,
) {
  EDENTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, isBlackAndWhite = isBlackAndWhite, content = content)
}
