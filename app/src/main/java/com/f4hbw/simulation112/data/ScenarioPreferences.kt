package com.f4hbw.simulation112.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.scenarioDataStore by preferencesDataStore(name = "scenario_prefs")

data class ScenarioSettings(
    val typeLieu: String,
    val densitePopulation: Int,
    val topographie: String,
    val precipitation: String,
    val ventForce: String,
    val ventChangeant: Boolean,
    val ventDirection: String,
    val temperature: Int,
    val moment: String,
    val difficulte: Int,
    val seed: Long,
    // Moyens extérieurs
    val samu: Boolean,
    val gendarmerie: Boolean,
    val police: Boolean,
    val voirie: Boolean,
    val mairie: Boolean,
    val pelican: Boolean,
    val milan: Boolean,
    val dragon: Boolean
)

object ScenarioPreferences {
    private val TYPE_LIEU = stringPreferencesKey("type_lieu")
    private val DENSITE_POP = intPreferencesKey("densite_population")
    private val TOPOGRAPHIE = stringPreferencesKey("topographie")
    private val PRECIPITATION = stringPreferencesKey("precipitation")
    private val VENT_FORCE = stringPreferencesKey("vent_force")
    private val VENT_CHANGEANT = booleanPreferencesKey("vent_changeant")
    private val VENT_DIRECTION = stringPreferencesKey("vent_direction")
    private val TEMPERATURE = intPreferencesKey("temperature")
    private val MOMENT = stringPreferencesKey("moment")
    private val DIFFICULTE = intPreferencesKey("difficulte")
    private val SEED = longPreferencesKey("seed")
    // Nouvelles clés
    private val SAMU = booleanPreferencesKey("samu")
    private val GENDARMERIE = booleanPreferencesKey("gendarmerie")
    private val POLICE = booleanPreferencesKey("police")
    private val VOIRIE = booleanPreferencesKey("voirie")
    private val MAIRIE = booleanPreferencesKey("mairie")
    private val PELICAN = booleanPreferencesKey("pelican")
    private val MILAN = booleanPreferencesKey("milan")
    private val DRAGON = booleanPreferencesKey("dragon")

    val settingsFlow: (Context) -> Flow<ScenarioSettings> = { context ->
        context.scenarioDataStore.data.map { prefs ->
            ScenarioSettings(
                typeLieu = prefs[TYPE_LIEU] ?: "Urbain",
                densitePopulation = prefs[DENSITE_POP] ?: 50,
                topographie = prefs[TOPOGRAPHIE] ?: "Plat",
                precipitation = prefs[PRECIPITATION] ?: "Modérée",
                ventForce = prefs[VENT_FORCE] ?: "Absent",
                ventChangeant = prefs[VENT_CHANGEANT] ?: false,
                ventDirection = prefs[VENT_DIRECTION] ?: "N",
                temperature = prefs[TEMPERATURE] ?: 20,
                moment = prefs[MOMENT] ?: "Matin",
                difficulte = prefs[DIFFICULTE] ?: 1,
                seed = prefs[SEED] ?: 0L,
                samu = prefs[SAMU] ?: true,
                gendarmerie = prefs[GENDARMERIE] ?: true,
                police = prefs[POLICE] ?: true,
                voirie = prefs[VOIRIE] ?: true,
                mairie = prefs[MAIRIE] ?: true,
                pelican = prefs[PELICAN] ?: true,
                milan = prefs[MILAN] ?: true,
                dragon = prefs[DRAGON] ?: true
            )
        }
    }

    suspend fun updateTypeLieu(context: Context, newType: String) = context.scenarioDataStore.edit { it[TYPE_LIEU] = newType }
    suspend fun updateDensitePopulation(context: Context, newValue: Int) = context.scenarioDataStore.edit { it[DENSITE_POP] = newValue }
    suspend fun updateTopographie(context: Context, newTopo: String) = context.scenarioDataStore.edit { it[TOPOGRAPHIE] = newTopo }
    suspend fun updatePrecipitation(context: Context, newValue: String) = context.scenarioDataStore.edit { it[PRECIPITATION] = newValue }
    suspend fun updateVentForce(context: Context, newValue: String) = context.scenarioDataStore.edit { it[VENT_FORCE] = newValue }
    suspend fun updateVentChangeant(context: Context, newValue: Boolean) = context.scenarioDataStore.edit { it[VENT_CHANGEANT] = newValue }
    suspend fun updateVentDirection(context: Context, newValue: String) = context.scenarioDataStore.edit { it[VENT_DIRECTION] = newValue }
    suspend fun updateTemperature(context: Context, newValue: Int) = context.scenarioDataStore.edit { it[TEMPERATURE] = newValue }
    suspend fun updateMoment(context: Context, newValue: String) = context.scenarioDataStore.edit { it[MOMENT] = newValue }
    suspend fun updateDifficulte(context: Context, newDiff: Int) = context.scenarioDataStore.edit { it[DIFFICULTE] = newDiff }
    suspend fun updateSeed(context: Context, newSeed: Long) = context.scenarioDataStore.edit { it[SEED] = newSeed }
    // Nouveaux updateurs
    suspend fun updateSamu(context: Context, avail: Boolean) = context.scenarioDataStore.edit { it[SAMU] = avail }
    suspend fun updateGendarmerie(context: Context, avail: Boolean) = context.scenarioDataStore.edit { it[GENDARMERIE] = avail }
    suspend fun updatePolice(context: Context, avail: Boolean) = context.scenarioDataStore.edit { it[POLICE] = avail }
    suspend fun updateVoirie(context: Context, avail: Boolean) = context.scenarioDataStore.edit { it[VOIRIE] = avail }
    suspend fun updateMairie(context: Context, avail: Boolean) = context.scenarioDataStore.edit { it[MAIRIE] = avail }
    suspend fun updatePelican(context: Context, avail: Boolean) = context.scenarioDataStore.edit { it[PELICAN] = avail }
    suspend fun updateMilan(context: Context, avail: Boolean) = context.scenarioDataStore.edit { it[MILAN] = avail }
    suspend fun updateDragon(context: Context, avail: Boolean) = context.scenarioDataStore.edit { it[DRAGON] = avail }
}
