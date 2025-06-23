// File: app/src/main/java/com/f4hbw/simulation112/ui/scenario/ScenarioScreen.kt
package com.f4hbw.simulation112.ui.scenario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.material3.SliderDefaults.colors as sliderColors
import androidx.compose.material3.SwitchDefaults.colors as switchColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.f4hbw.simulation112.ui.theme.Red90
import com.f4hbw.simulation112.viewmodel.ScenarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenarioScreen(
    viewModel: ScenarioViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val scrollState = rememberScrollState()    // ← plus de "by"

    var expType   by remember { mutableStateOf(false) }
    var expTopo   by remember { mutableStateOf(false) }
    var expPrec   by remember { mutableStateOf(false) }
    var expVentF  by remember { mutableStateOf(false) }
    var expVentD  by remember { mutableStateOf(false) }
    var expMoment by remember { mutableStateOf(false) }

    val types      = listOf("Urbain","Rural","Industriel","Montagnard")
    val topos      = listOf("Plat","Vallonné","Montagneux")
    val precs      = listOf("Absente","Faible","Modérée","Forte","Extrême")
    val ventForces = listOf("Absent","<15 km/h","<50 km/h","<80 km/h","<110 km/h",">110 km/h")
    val ventDirs   = listOf("N","NE","E","SE","S","SO","O","NO")
    val moments    = listOf("Matin","Midi","Soir","Nuit")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Réglages du scénario", color = Red90, style = MaterialTheme.typography.headlineSmall)

        @Composable
        fun CardDropdown(content: @Composable BoxScope.() -> Unit) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                border   = BorderStroke(1.dp, Red90),
                shape    = RoundedCornerShape(8.dp),
                colors   = elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(Modifier.padding(8.dp), content = content)
            }
        }

        // Type de lieu
        CardDropdown {
            ExposedDropdownMenuBox(expanded = expType, onExpandedChange = { expType = !expType }) {
                TextField(
                    value       = settings.typeLieu,
                    onValueChange = {},
                    label       = { Text("Type de lieu") },
                    readOnly    = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expType) },
                    colors      = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                )
                ExposedDropdownMenu(expanded = expType, onDismissRequest = { expType = false }) {
                    types.forEach { opt ->
                        DropdownMenuItem(
                            text    = { Text(opt) },
                            onClick = {
                                viewModel.updateTypeLieu(opt)
                                expType = false
                            }
                        )
                    }
                }
            }
        }

        // Densité
        Column {
            Text("Densité : ${settings.densitePopulation}% ", fontSize = 16.sp)
            Slider(
                value        = settings.densitePopulation.toFloat(),
                onValueChange = { viewModel.updateDensitePopulation(it.toInt()) },
                valueRange   = 0f..100f,
                steps        = 4,
                colors       = sliderColors(activeTrackColor = Red90, thumbColor = Red90)
            )
        }

        // (… répète les mêmes patterns pour les autres dropdowns…)

        // Température
        Column {
            Text("Température : ${settings.temperature}°C", fontSize = 16.sp)
            Slider(
                value        = settings.temperature.toFloat(),
                onValueChange = { viewModel.updateTemperature(it.toInt()) },
                valueRange   = -20f..50f,
                steps        = 14,
                colors       = sliderColors(activeTrackColor = Red90, thumbColor = Red90)
            )
        }

        // Difficulté
        Column {
            Text("Difficulté : ${settings.difficulte}", fontSize = 16.sp)
            Slider(
                value        = settings.difficulte.toFloat(),
                onValueChange = { viewModel.updateDifficulte(it.toInt()) },
                valueRange   = 1f..5f,
                steps        = 3,
                colors       = sliderColors(activeTrackColor = Red90, thumbColor = Red90)
            )
        }

        // Seed
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Text("Graine :", fontSize = 16.sp)
            Text(settings.seed.toString(), fontSize = 16.sp, color = Red90)
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.generateAndSaveSeed() },
            colors  = ButtonDefaults.buttonColors(containerColor = Red90),
            modifier= Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape   = RoundedCornerShape(8.dp)
        ) {
            Text("Générer la graine", color = Color.White, fontSize = 16.sp)
        }
    }
}
