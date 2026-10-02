package com.illit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.illit.app.ui.theme.IllitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            IllitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F0EB)
                ) {
                    InfiniteCanvasScreen()
                }
            }
        }
    }
}

@Composable
fun InfiniteCanvasScreen() {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var paths by remember { mutableStateOf(listOf<Path>()) }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var currentColor by remember { mutableStateOf(Color.Black) }
    var strokeWidth by remember { mutableStateOf(5f) }
    
    val colors = listOf(
        Color.Black, Color.Red, Color.Blue, Color.Green,
        Color.Yellow, Color(0xFFFF5722), Color(0xFF9C27B0)
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F0EB))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.1f, 10f)
                        offset += pan
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { startOffset ->
                            val path = Path()
                            val worldOffset = (startOffset - offset) / scale
                            path.moveTo(worldOffset.x, worldOffset.y)
                            currentPath = path
                        },
                        onDrag = { change, _ ->
                            val worldOffset = (change.position - offset) / scale
                            currentPath?.lineTo(worldOffset.x, worldOffset.y)
                            currentPath = currentPath
                        },
                        onDragEnd = {
                            currentPath?.let { path ->
                                paths = paths + path
                            }
                            currentPath = null
                        }
                    )
                }
        ) {
            val matrix = androidx.compose.ui.graphics.drawscope.withTransform
            translate(offset.x, offset.y) {
                scale(scale, scale, pivot = Offset.Zero) {
                    paths.forEach { path ->
                        drawPath(
                            path = path,
                            color = currentColor,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                    currentPath?.let { path ->
                        drawPath(
                            path = path,
                            color = currentColor,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
            }
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FloatingActionButton(
                onClick = { },
                containerColor = Color(0xFF4CAF50),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Penna", tint = Color.White)
            }
            
            FloatingActionButton(
                onClick = {
                    if (paths.isNotEmpty()) paths = paths.dropLast(1)
                },
                containerColor = Color(0xFFFF9800),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Gomma", tint = Color.White)
            }
            
            FloatingActionButton(
                onClick = {
                    if (paths.isNotEmpty()) paths = paths.dropLast(1)
                },
                containerColor = Color(0xFFD6BEEA),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Undo, contentDescription = "Annulla", tint = Color(0xFF3D2C1E))
            }
            
            FloatingActionButton(
                onClick = {
                    scale = 1f
                    offset = Offset.Zero
                },
                containerColor = Color(0xFFB7C96A),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color(0xFF3D2C1E))
            }
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, shape = CircleShape)
                        .clickable { currentColor = color }
                )
            }
        }
    }
}
