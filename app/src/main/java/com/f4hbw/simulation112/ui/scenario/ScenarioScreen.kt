package com.f4hbw.simulation112.ui.scenario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.f4hbw.simulation112.viewmodel.ScenarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenarioScreen(
    viewModel: ScenarioViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val scrollState = rememberScrollState()

    // États des menus déroulants
    var expandedType by remember { mutableStateOf(false) }
    var expandedTopo by remember { mutableStateOf(false) }
    var expandedPrec by remember { mutableStateOf(false) }
    var expandedVentForce by remember { mutableStateOf(false) }
    var expandedVentDir by remember { mutableStateOf(false) }
    var expandedMoment by remember { mutableStateOf(false) }

    // Listes d’options
    val types = listOf("Urbain", "Rural", "Industriel", "Montagnard")
    val topos = listOf("Plat", "Vallonné", "Montagneux")
    val precs = listOf("Absente", "Faible", "Modérée", "Forte", "Extrême")
    val ventForces = listOf("Absent", "< 15 km/h", "< 50 km/h", "< 80 km/h", "< 110 km/h", "> 110 km/h")
    val ventDirs = listOf("N", "NE", "E", "SE", "S", "SO", "O", "NO")
    val moments = listOf("Matin", "Midi", "Soir", "Nuit")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Titre
        Text("Réglages du scénario", style = MaterialTheme.typography.headlineSmall)

        // 1. Type de lieu
        ExposedDropdownMenuBox(
            expanded = expandedType,
            onExpandedChange = { expandedType = !expandedType }
        ) {
            TextField(
                value = settings.typeLieu,
                onValueChange = { },
                label = { Text("Type de lieu") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedType,
                onDismissRequest = { expandedType = false }
            ) {
                types.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.updateTypeLieu(option)
                            expandedType = false
                        }
                    )
                }
            }
        }

        // 2. Densité de population
        Column {
            Text("Densité de population : ${settings.densitePopulation}%", fontSize = 16.sp)
            Slider(
                value = settings.densitePopulation.toFloat(),
                onValueChange = { viewModel.updateDensitePopulation(it.toInt()) },
                valueRange = 0f..100f,
                steps = 4,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 3. Topographie
        ExposedDropdownMenuBox(
            expanded = expandedTopo,
            onExpandedChange = { expandedTopo = !expandedTopo }
        ) {
            TextField(
                value = settings.topographie,
                onValueChange = { },
                label = { Text("Topographie") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTopo) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedTopo,
                onDismissRequest = { expandedTopo = false }
            ) {
                topos.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.updateTopographie(option)
                            expandedTopo = false
                        }
                    )
                }
            }
        }

        // 4. Précipitations
        ExposedDropdownMenuBox(
            expanded = expandedPrec,
            onExpandedChange = { expandedPrec = !expandedPrec }
        ) {
            TextField(
                value = settings.precipitation,
                onValueChange = { },
                label = { Text("Précipitations") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrec) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedPrec,
                onDismissRequest = { expandedPrec = false }
            ) {
                precs.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.updatePrecipitation(option)
                            expandedPrec = false
                        }
                    )
                }
            }
        }

        // 5. Force du vent
        ExposedDropdownMenuBox(
            expanded = expandedVentForce,
            onExpandedChange = { expandedVentForce = !expandedVentForce }
        ) {
            TextField(
                value = settings.ventForce,
                onValueChange = { },
                label = { Text("Force du vent") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVentForce) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedVentForce,
                onDismissRequest = { expandedVentForce = false }
            ) {
                ventForces.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.updateVentForce(option)
                            expandedVentForce = false
                        }
                    )
                }
            }
        }

        // 6. Vent changeant
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Vent changeant")
            Spacer(Modifier.weight(1f))
            Switch(
                checked = settings.ventChangeant,
                onCheckedChange = { viewModel.updateVentChangeant(it) }
            )
        }

        // 7. Direction du vent
        ExposedDropdownMenuBox(
            expanded = expandedVentDir,
            onExpandedChange = { if (!settings.ventChangeant) expandedVentDir = !expandedVentDir }
        ) {
            TextField(
                value = settings.ventDirection,
                onValueChange = { },
                label = { Text("Direction du vent") },
                readOnly = true,
                enabled = !settings.ventChangeant,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVentDir) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedVentDir,
                onDismissRequest = { expandedVentDir = false }
            ) {
                ventDirs.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.updateVentDirection(option)
                            expandedVentDir = false
                        }
                    )
                }
            }
        }

        // 8. Température
        OutlinedTextField(
            value = settings.temperature.toString(),
            onValueChange = { str -> str.toIntOrNull()?.let { viewModel.updateTemperature(it) } },
            label = { Text("Température (°C)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // 9. Moment de la journée
        ExposedDropdownMenuBox(
            expanded = expandedMoment,
            onExpandedChange = { expandedMoment = !expandedMoment }
        ) {
            TextField(
                value = settings.moment,
                onValueChange = { },
                label = { Text("Moment") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMoment) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedMoment,
                onDismissRequest = { expandedMoment = false }
            ) {
                moments.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.updateMoment(option)
                            expandedMoment = false
                        }
                    )
                }
            }
        }

        // 10. Difficulté
        Column {
            Text("Niveau de difficulté : ${settings.difficulte}", fontSize = 16.sp)
            Slider(
                value = settings.difficulte.toFloat(),
                onValueChange = { viewModel.updateDifficulte(it.toInt()) },
                valueRange = 1f..5f,
                steps = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 11. Graine (seed)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Graine :", fontSize = 16.sp)
            Text(settings.seed.toString(), fontSize = 16.sp)
        }

        // 12. Moyens extérieurs disponibles
        Text("Moyens extérieurs disponibles", fontSize = 18.sp)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FunToggle("SAMU", settings.samu, viewModel::updateSamu)
            FunToggle("GENDARMERIE", settings.gendarmerie, viewModel::updateGendarmerie)
            FunToggle("POLICE", settings.police, viewModel::updatePolice)
            FunToggle("VOIRIE", settings.voirie, viewModel::updateVoirie)
            FunToggle("MAIRIE", settings.mairie, viewModel::updateMairie)
            FunToggle("PELICAN", settings.pelican, viewModel::updatePelican)
            FunToggle("MILAN", settings.milan, viewModel::updateMilan)
            FunToggle("DRAGON", settings.dragon, viewModel::updateDragon)
        }

        Spacer(Modifier.height(32.dp))

        // Bouton de génération de graine
        Button(
            onClick = { viewModel.generateAndSaveSeed() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Générer la graine")
        }
    }
}

@Composable
private fun FunToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
