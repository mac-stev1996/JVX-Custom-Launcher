package com.jvx.launcher

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class ThemeOption(val name: String, val background: Color, val accent: Color, val subtitle: String)

private val themes = listOf(
    ThemeOption("BMW M Deportivo", Color(0xFF101820), Color(0xFF00AEEF), "M PERFORMANCE"),
    ThemeOption("Mercedes Futurista", Color(0xFF10141B), Color(0xFFD8DDE5), "LUXURY DIGITAL"),
    ThemeOption("Tesla Minimalista", Color(0xFF111111), Color(0xFFE53935), "PURE MINIMAL"),
    ThemeOption("Gaming RGB", Color(0xFF100D20), Color(0xFFB44CFF), "RGB MODE"),
    ThemeOption("Porsche Racing", Color(0xFF171717), Color(0xFFFFC400), "TRACK EDITION"),
    ThemeOption("Audi Virtual Cockpit", Color(0xFF0C1724), Color(0xFF29B6F6), "VIRTUAL COCKPIT"),
    ThemeOption("Lamborghini", Color(0xFF15190E), Color(0xFFB7D600), "DARK SPORT"),
    ThemeOption("Cyberpunk", Color(0xFF160D22), Color(0xFFFF3CAC), "NIGHT CITY"),
    ThemeOption("Luxury Premium", Color(0xFF17130F), Color(0xFFD6B36A), "EXECUTIVE"),
    ThemeOption("Futuristic 2050", Color(0xFF071A20), Color(0xFF00E5C7), "FUTURE DRIVE")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
            android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
            android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        setContent { LauncherScreen() }
    }
}

@Composable
private fun LauncherScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("jvx_launcher", 0) }
    var selectedIndex by remember { mutableIntStateOf(prefs.getInt("theme", 0).coerceIn(themes.indices)) }
    var showThemes by remember { mutableStateOf(false) }
    val theme = themes[selectedIndex]

    MaterialTheme(colorScheme = darkColorScheme(
        background = theme.background,
        surface = Color(0xFF20252D),
        primary = theme.accent,
        onBackground = Color.White,
        onSurface = Color.White
    )) {
        Row(
            modifier = Modifier.fillMaxSize().background(theme.background).padding(22.dp),
            horizontalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Column(Modifier.weight(1.25f).fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("JVX", color = theme.accent, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text(theme.subtitle, color = Color.LightGray, fontSize = 11.sp, letterSpacing = 2.sp)
                }
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Bienvenido", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Light)
                    Text(themes[selectedIndex].name, color = theme.accent, fontSize = 16.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ActionTile("Cambiar tema", "Temas") { showThemes = true }
                    ActionTile("Launcher JVX", "Original") { openOriginalLauncher(context) }
                }
            }

            Column(Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                val time = rememberClockText()
                Text(time, color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Light, modifier = Modifier.align(Alignment.End))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AppTile("Maps", "Navegación", theme.accent, Modifier.weight(1f)) { launchPackage(context, "com.google.android.apps.maps") }
                        AppTile("YouTube", "Video", theme.accent, Modifier.weight(1f)) { launchPackage(context, "com.google.android.youtube") }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AppTile("Spotify", "Música", theme.accent, Modifier.weight(1f)) { launchPackage(context, "com.spotify.music") }
                        AppTile("Netflix", "Entretenimiento", theme.accent, Modifier.weight(1f)) { launchPackage(context, "com.netflix.mediaclient") }
                    }
                }
                Text("1280 × 720  •  JVX CUSTOM LAUNCHER", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.align(Alignment.End))
            }
        }

        if (showThemes) {
            AlertDialog(
                onDismissRequest = { showThemes = false },
                title = { Text("Seleccionar tema") },
                text = {
                    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.height(330.dp)) {
                        items(themes) { item ->
                            Text(
                                item.name,
                                color = if (item == theme) item.accent else Color.White,
                                modifier = Modifier.fillMaxWidth().padding(9.dp).clickable {
                                    selectedIndex = themes.indexOf(item)
                                    prefs.edit().putInt("theme", selectedIndex).apply()
                                    showThemes = false
                                }
                            )
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { showThemes = false }) { Text("Cerrar") } }
            )
        }
    }
}

@Composable
private fun rememberClockText(): String {
    var value by remember { mutableStateOf("--:--") }
    LaunchedEffect(Unit) {
        while (true) {
            val now = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
            value = now.format(java.util.Date())
            kotlinx.coroutines.delay(1000)
        }
    }
    return value
}

@Composable
private fun ActionTile(title: String, subtitle: String, onClick: () -> Unit) {
    Column(
        Modifier.background(Color(0xFF20252D), RoundedCornerShape(14.dp)).clickable(onClick = onClick).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
        Text(subtitle, color = Color.LightGray, fontSize = 11.sp)
    }
}

@Composable
private fun AppTile(title: String, subtitle: String, accent: Color, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier.background(Color(0xFF20252D), RoundedCornerShape(14.dp)).clickable(onClick = onClick).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(title, color = accent, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = Color.LightGray, fontSize = 11.sp)
    }
}

private fun launchPackage(context: android.content.Context, packageName: String) {
    val intent = context.packageManager.getLaunchIntentForPackage(packageName)
    if (intent != null) context.startActivity(intent)
    else Toast.makeText(context, "La aplicación no está instalada", Toast.LENGTH_SHORT).show()
}

private fun openOriginalLauncher(context: android.content.Context) {
    val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "No se pudo abrir el launcher original desde esta configuración", Toast.LENGTH_LONG).show()
    }
}
