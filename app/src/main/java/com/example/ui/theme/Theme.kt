package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

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
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  EDENTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
