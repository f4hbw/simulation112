package com.f4hbw.simulation112.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.f4hbw.simulation112.R

private val SoftRed = Color(0xFFCC3333)

data class QuickAction(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val contentDescription: String,
    val containerColor: Color? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    pseudo: String,
    onProfileClick: () -> Unit,
    onAvailabilityClick: () -> Unit,
    onScenarioClick: () -> Unit,
    onAboutClick: () -> Unit,
    onSettingsClick: (() -> Unit)? = null,
    onLogout: () -> Unit
) {
    // État local de disponibilité
    var isAvailable by remember { mutableStateOf(false) }
    val toggleAvailability = {
        isAvailable = !isAvailable
        onAvailabilityClick()
    }
    val availabilityLabel = if (isAvailable) "Disponible" else "Indisponible"
    val availabilityColor = if (isAvailable) Color(0xFF4CAF50) else Color.Gray
    val settingsAction = onSettingsClick ?: {}

    val quickActions = listOf(
        QuickAction("Mon profil",      Icons.Default.Person,    onProfileClick,    "Accéder à mon profil"),
        QuickAction(
            availabilityLabel,
            Icons.Default.Check,
            toggleAvailability,
            "Changer état de disponibilité",
            availabilityColor
        ),
        QuickAction("Scénario",        Icons.Default.PlayArrow, onScenarioClick,   "Démarrer un scénario"),
        QuickAction("Réglages",        Icons.Default.Settings,  settingsAction,    "Accéder aux réglages")
    )

    val aboutAction = QuickAction(
        label              = "À propos",
        icon               = Icons.Default.Info,
        onClick            = onAboutClick,
        contentDescription = "Informations sur l'application"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Simulation112", color = Color.White, fontWeight = FontWeight.Medium)
                },
                actions = {
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.semantics {
                            role = Role.Button
                            contentDescription = "Se déconnecter"
                        }
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SoftRed)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement   = Arrangement.spacedBy(12.dp),
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            // En-tête avec le pseudo
            Column(
                horizontalAlignment   = Alignment.CenterHorizontally,
                verticalArrangement   = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text      = "Bonjour, $pseudo",
                    style     = MaterialTheme.typography.headlineLarge.copy(
                        fontSize   = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color     = SoftRed,
                    textAlign = TextAlign.Center
                )
                Text(
                    text      = "Que voulez-vous faire aujourd’hui ?",
                    style     = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Bipeur avec overlay du statut
            Box(modifier = Modifier.size(540.dp)) {
                Image(
                    painter            = painterResource(R.drawable.ic_beeper),
                    contentDescription = "Bipeur pompier",
                    modifier           = Modifier.fillMaxSize()
                )
                Text(
                    text      = availabilityLabel,
                    color     = Color.Black,
                    fontSize  = 32.sp,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier
                        .offset(x = 123.dp, y = 216.dp)
                        .width(293.dp)
                        .height(139.dp)
                )
            }

            // Grille des 4 actions + bouton À propos
            Column(
                modifier             = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LazyVerticalGrid(
                    columns              = GridCells.Fixed(2),
                    modifier             = Modifier.fillMaxWidth(),
                    contentPadding       = PaddingValues(vertical = 8.dp),
                    horizontalArrangement= Arrangement.spacedBy(12.dp),
                    verticalArrangement  = Arrangement.spacedBy(12.dp)
                ) {
                    items(quickActions) { action ->
                        QuickActionCard(action)
                    }
                }
                // Bouton "À propos" centré, même taille que les autres
                Row(
                    modifier             = Modifier.fillMaxWidth(),
                    horizontalArrangement= Arrangement.Center
                ) {
                    Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                        QuickActionCard(aboutAction)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickActionCard(action: QuickAction) {
    val background = action.containerColor ?: MaterialTheme.colorScheme.surface
    Card(
        modifier   = Modifier
            .fillMaxWidth()
            .aspectRatio(2.5f)
            .semantics {
                role               = Role.Button
                contentDescription = action.contentDescription
            },
        shape      = RoundedCornerShape(10.dp),
        border     = BorderStroke(1.dp, SoftRed.copy(alpha = 0.3f)),
        elevation  = elevatedCardElevation(defaultElevation = 2.dp),
        colors     = CardDefaults.cardColors(containerColor = background)
    ) {
        Box(
            modifier          = Modifier
                .fillMaxSize()
                .clickable { action.onClick() }
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment   = Alignment.CenterHorizontally,
                verticalArrangement   = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector        = action.icon,
                    contentDescription = null,
                    modifier           = Modifier.size(32.dp),
                    tint               = SoftRed
                )
                Text(
                    text      = action.label,
                    textAlign = TextAlign.Center,
                    style     = MaterialTheme.typography.bodyMedium.copy(
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color     = MaterialTheme.colorScheme.onSurface,
                    maxLines  = 2
                )
            }
        }
    }
}
