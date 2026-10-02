package com.illit.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Sky = Color(0xFF7FB9E6)
val Lavender = Color(0xFFD6BEEA)
val Butter = Color(0xFFF4D77A)
val Matcha = Color(0xFFB7C96A)
val Pink = Color(0xFFF98BA9)
val Tangerine = Color(0xFFFF8F45)

val BeigeScuro = Color(0xFFF5F0EB)
val BiancoCarta = Color(0xFFFFFBF5)
val TestoScuro = Color(0xFF3D2C1E)
val TestoChiaro = Color(0xFF8B7B6B)

@Composable
fun IllitTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Sky,
            secondary = Lavender,
            tertiary = Butter,
            background = BeigeScuro,
            surface = BiancoCarta,
            onPrimary = Color.White,
            onSecondary = TestoScuro,
            onBackground = TestoScuro,
            onSurface = TestoScuro
        ),
        content = content
    )
}
