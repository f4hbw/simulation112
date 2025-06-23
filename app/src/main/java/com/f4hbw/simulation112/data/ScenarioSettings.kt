package com.f4hbw.simulation112.data

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
    val seed: Long
)
