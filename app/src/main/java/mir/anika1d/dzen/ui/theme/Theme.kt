package mir.anika1d.dzen.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Color.White,
    background = Color.Black,
)

private val LightColorPalette = lightColorScheme(
    primary = PurpleGrey40,
    secondary = Purple40,
    tertiary = Color.White,
    background = Color.White


)

@Composable
fun DzenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(
                (view.context as Activity).window,
                view
            ).isAppearanceLightStatusBars = darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}