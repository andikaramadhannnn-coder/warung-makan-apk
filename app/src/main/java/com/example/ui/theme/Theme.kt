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

private val DarkColorScheme = darkColorScheme(
    primary = TerracottaDarkPrimary,
    onPrimary = TerracottaDarkOnPrimary,
    primaryContainer = TerracottaDarkPrimaryContainer,
    onPrimaryContainer = TerracottaDarkOnPrimaryContainer,
    secondary = SaffronSecondary,
    onSecondary = SaffronOnSecondary,
    secondaryContainer = SaffronSecondaryContainer,
    onSecondaryContainer = SaffronOnSecondaryContainer,
    background = WarungDarkBackground,
    surface = WarungDarkSurface,
    surfaceVariant = WarungDarkSurfaceVariant,
    onBackground = WarungDarkOnSurface,
    onSurface = WarungDarkOnSurface,
    outline = WarungDarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = TerracottaOnPrimary,
    primaryContainer = TerracottaPrimaryContainer,
    onPrimaryContainer = TerracottaOnPrimaryContainer,
    secondary = SaffronSecondary,
    onSecondary = SaffronOnSecondary,
    secondaryContainer = SaffronSecondaryContainer,
    onSecondaryContainer = SaffronOnSecondaryContainer,
    tertiary = BrownTertiary,
    onTertiary = BrownOnTertiary,
    tertiaryContainer = BrownTertiaryContainer,
    onTertiaryContainer = BrownOnTertiaryContainer,
    background = WarungBackground,
    surface = WarungSurface,
    surfaceVariant = WarungSurfaceVariant,
    onBackground = WarungOnBackground,
    onSurface = WarungOnSurface,
    onSurfaceVariant = WarungOnSurfaceVariant,
    outline = WarungOutline,
    outlineVariant = WarungOutlineVariant
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // We intentionally disable dynamicColor so the warung culinary branding remains distinct & consistent
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
