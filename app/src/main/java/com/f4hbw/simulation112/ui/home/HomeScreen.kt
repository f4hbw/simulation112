package com.f4hbw.simulation112.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.f4hbw.simulation112.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    pseudo: String,
    onLogout: () -> Unit,
    onScenarioClick: () -> Unit   // ← nouveau callback
) {
    var available by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bienvenue, $pseudo !") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFB71C1C),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Déconnexion",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Première ligne
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HomeActionButton(Icons.Default.Person, "Mon profil") { /* TODO */ }
                    HomeActionButton(Icons.Default.PlayArrow, "Paramètres de simulation") {
                        onScenarioClick()  // ← navigation déclenchée ici
                    }
                }
                Spacer(Modifier.height(16.dp))

                // Deuxième ligne
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val (icon, label, border) = if (available) {
                        Triple(Icons.Default.CheckCircle, "Disponible", Color(0xFF388E3C))
                    } else {
                        Triple(Icons.Default.Close, "Indisponible", Color.Gray)
                    }
                    HomeActionButton(icon, label, border) { available = !available }
                    HomeActionButton(Icons.Default.Settings, "Paramètres") { /* TODO */ }
                }
                Spacer(Modifier.height(16.dp))

                // Troisième ligne
                HomeActionButton(Icons.Default.Info, "À propos") { /* TODO */ }
            }

            // Image du bipeur centrée en bas, taille doublée (720.dp)
            Image(
                painter = painterResource(R.drawable.bip_pompier),
                contentDescription = "Bipeur pompier",
                modifier = Modifier.size(720.dp)
            )
        }
    }
}

@Composable
fun HomeActionButton(
    icon: ImageVector,
    label: String,
    borderColor: Color = Color(0xFFFFCDD2),
    onClick: () -> Unit
) {
    val buttonSize = 300.dp * 0.8f
    Card(
        modifier = Modifier
            .size(buttonSize)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = borderColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}
