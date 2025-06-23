package com.f4hbw.simulation112.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.f4hbw.simulation112.data.ScenarioPreferences
import com.f4hbw.simulation112.data.ScenarioSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.zip.CRC32
import kotlin.random.Random

class ScenarioViewModel(app: Application) : AndroidViewModel(app) {
    private val ctx = app.applicationContext

    val settings: StateFlow<ScenarioSettings> =
        ScenarioPreferences.settingsFlow(ctx)
            .stateIn(viewModelScope, SharingStarted.Lazily, ScenarioSettings(
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
                seed = 0L
            ))

    fun updateTypeLieu(new: String)        = viewModelScope.launch { ScenarioPreferences.updateTypeLieu(ctx,new) }
    fun updateDensitePopulation(v: Int)    = viewModelScope.launch { ScenarioPreferences.updateDensitePopulation(ctx,v) }
    fun updateTopographie(v: String)       = viewModelScope.launch { ScenarioPreferences.updateTopographie(ctx,v) }
    fun updatePrecipitation(v: String)     = viewModelScope.launch { ScenarioPreferences.updatePrecipitation(ctx,v) }
    fun updateVentForce(v: String)         = viewModelScope.launch { ScenarioPreferences.updateVentForce(ctx,v) }
    fun updateVentChangeant(v: Boolean)    = viewModelScope.launch { ScenarioPreferences.updateVentChangeant(ctx,v) }
    fun updateVentDirection(v: String)     = viewModelScope.launch { ScenarioPreferences.updateVentDirection(ctx,v) }
    fun updateTemperature(v: Int)          = viewModelScope.launch { ScenarioPreferences.updateTemperature(ctx,v) }
    fun updateMoment(v: String)            = viewModelScope.launch { ScenarioPreferences.updateMoment(ctx,v) }
    fun updateDifficulte(v: Int)           = viewModelScope.launch { ScenarioPreferences.updateDifficulte(ctx,v) }

    /** Génère + sauvegarde la seed */
    fun generateAndSaveSeed() = viewModelScope.launch {
        val c = ScenarioPreferences.settingsFlow(ctx).first()
        val prefix = listOf(
            c.difficulte,
            c.precipitation, c.ventForce, c.ventChangeant,
            c.ventDirection, c.temperature, c.moment,
            c.typeLieu, c.densitePopulation, c.topographie
        ).joinToString("_")
        val hash = CRC32().apply { update(prefix.toByteArray()) }.value
        val suffix = Random.nextInt(0, 1_000_000)
        ScenarioPreferences.updateSeed(ctx, hash * 1_000_000L + suffix)
    }
}
