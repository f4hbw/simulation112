// File: app/src/main/java/com/f4hbw/simulation112/viewmodel/ScenarioViewModel.kt
package com.f4hbw.simulation112.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.f4hbw.simulation112.data.ScenarioPreferences
import com.f4hbw.simulation112.data.ScenarioSettings
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.zip.CRC32
import kotlin.random.Random

class ScenarioViewModel(app: Application) : AndroidViewModel(app) {
    private val context = app.applicationContext

    val settings: StateFlow<ScenarioSettings> =
        ScenarioPreferences.settingsFlow(context)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ScenarioSettings(
                    typeLieu = "Urbain",
                    densitePopulation = 50,
                    topographie = "Plat",
                    precipitation = "Modérée",
                    ventForce = "Absent",
                    ventChangeant = false,
                    ventDirection = "N",
                    temperature = 20,
                    moment = "Matin",
                    difficulte = 1,
                    seed = 0L,
                    samu = true,
                    gendarmerie = true,
                    police = true,
                    voirie = true,
                    mairie = true,
                    pelican = true,
                    milan = true,
                    dragon = true
                )
            )

    // … Tous tes `updateXxx()` inchangés …

    /**
     * Génère une seed avec :
     * - un **préfixe déterministe** (hash CRC32 du seul préfixe de paramètres)
     * - un **suffixe aléatoire** (6 chiffres)
     */
    fun generateAndSaveSeed() = viewModelScope.launch {
        val current = ScenarioPreferences.settingsFlow(context).first()

        // 1) Construit la chaîne de préfixe (uniquement paramètres)
        val prefixString = buildString {
            append(current.difficulte)
            append("_${current.precipitation}")
            append("_${current.ventForce}")
            append("_${current.ventChangeant}")
            append("_${current.ventDirection}")
            append("_${current.temperature}")
            append("_${current.moment}")
            append("_${current.typeLieu}")
            append("_${current.densitePopulation}")
            append("_${current.topographie}")
            append("_${current.samu}")
            append("_${current.gendarmerie}")
            append("_${current.police}")
            append("_${current.voirie}")
            append("_${current.mairie}")
            append("_${current.pelican}")
            append("_${current.milan}")
            append("_${current.dragon}")
        }

        // 2) Hash du préfixe seul
        val prefixHash = CRC32().apply {
            update(prefixString.toByteArray())
        }.value  // [0 .. 2^32-1]

        // 3) Génère un suffixe aléatoire à 6 chiffres (000000 .. 999999)
        val randomSuffix = Random.nextInt(0, 1_000_000)

        // 4) Concatène numériquement : prefixHash * 1_000_000 + randomSuffix
        val newSeed = prefixHash * 1_000_000L + randomSuffix

        // 5) Sauvegarde
        ScenarioPreferences.updateSeed(context, newSeed)
    }
}
