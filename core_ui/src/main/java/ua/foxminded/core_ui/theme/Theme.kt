package ua.foxminded.core_ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorPalette = darkColors(
    primary = Color.Black,
    onPrimary = Color.White,
    primaryVariant = GraniteGray,
    secondaryVariant = CaribbeanGreen,
    onSurface = Color.White,
    onSecondary = RaisinBlack
)

private val LightColorPalette = lightColors(
    primary = ChromeYellow,
    primaryVariant = SilverChalice,
    onPrimary = Color.White,
    secondary = DarkCharcoal,
    onSecondary = PhilippineGray,
    secondaryVariant = AntiFlashWhite,
    surface = Color.White,
    onSurface = ChromeYellow
)

@Composable
fun TrackerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        SideEffect {
            window.statusBarColor = colors.primary.toArgb()
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}