package com.f4hbw.simulation112.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "scenario_prefs")

object ScenarioPreferences {
    private val TYPE_LIEU          = stringPreferencesKey("type_lieu")
    private val DENSITE_POPULATION = intPreferencesKey("densite_population")
    private val TOPOGRAPHIE        = stringPreferencesKey("topographie")
    private val PRECIPITATION      = stringPreferencesKey("precipitation")
    private val VENT_FORCE         = stringPreferencesKey("vent_force")
    private val VENT_CHANGEANT     = booleanPreferencesKey("vent_changeant")
    private val VENT_DIRECTION     = stringPreferencesKey("vent_direction")
    private val TEMPERATURE        = intPreferencesKey("temperature")
    private val MOMENT             = stringPreferencesKey("moment")
    private val DIFFICULTE         = intPreferencesKey("difficulte")
    private val SEED               = longPreferencesKey("seed")

    fun settingsFlow(context: Context): Flow<ScenarioSettings> =
        context.dataStore.data.map { prefs ->
            ScenarioSettings(
                typeLieu          = prefs[TYPE_LIEU]          ?: "Urbain",
                densitePopulation = prefs[DENSITE_POPULATION] ?: 50,
                topographie       = prefs[TOPOGRAPHIE]        ?: "Plat",
                precipitation     = prefs[PRECIPITATION]      ?: "Modérée",
                ventForce         = prefs[VENT_FORCE]         ?: "Absent",
                ventChangeant     = prefs[VENT_CHANGEANT]     ?: false,
                ventDirection     = prefs[VENT_DIRECTION]     ?: "N",
                temperature       = prefs[TEMPERATURE]        ?: 20,
                moment            = prefs[MOMENT]             ?: "Matin",
                difficulte        = prefs[DIFFICULTE]         ?: 1,
                seed              = prefs[SEED]               ?: 0L
            )
        }

    suspend fun updateTypeLieu(context: Context, newValue: String) =
        context.dataStore.edit { it[TYPE_LIEU] = newValue }
    suspend fun updateDensitePopulation(context: Context, newValue: Int) =
        context.dataStore.edit { it[DENSITE_POPULATION] = newValue }
    suspend fun updateTopographie(context: Context, newValue: String) =
        context.dataStore.edit { it[TOPOGRAPHIE] = newValue }
    suspend fun updatePrecipitation(context: Context, newValue: String) =
        context.dataStore.edit { it[PRECIPITATION] = newValue }
    suspend fun updateVentForce(context: Context, newValue: String) =
        context.dataStore.edit { it[VENT_FORCE] = newValue }
    suspend fun updateVentChangeant(context: Context, newValue: Boolean) =
        context.dataStore.edit { it[VENT_CHANGEANT] = newValue }
    suspend fun updateVentDirection(context: Context, newValue: String) =
        context.dataStore.edit { it[VENT_DIRECTION] = newValue }
    suspend fun updateTemperature(context: Context, newValue: Int) =
        context.dataStore.edit { it[TEMPERATURE] = newValue }
    suspend fun updateMoment(context: Context, newValue: String) =
        context.dataStore.edit { it[MOMENT] = newValue }
    suspend fun updateDifficulte(context: Context, newValue: Int) =
        context.dataStore.edit { it[DIFFICULTE] = newValue }
    suspend fun updateSeed(context: Context, newValue: Long) =
        context.dataStore.edit { it[SEED] = newValue }
}
