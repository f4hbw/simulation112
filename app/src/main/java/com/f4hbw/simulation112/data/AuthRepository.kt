// File: app/src/main/java/com/f4hbw/simulation112/data/AuthRepository.kt
package com.f4hbw.simulation112.data

import com.f4hbw.simulation112.viewmodel.AuthUiState
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// --- Modèles de requête/réponse pour l'API auth ---
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val pseudo: String)

interface AuthApi {
    @POST("/api/auth/login")
    suspend fun login(@Body req: LoginRequest): LoginResponse

    @POST("/api/auth/register")
    suspend fun register(@Body req: LoginRequest): LoginResponse

    // Autres appels (reset-password, etc.)...
}

class AuthRepository {
    private val api: AuthApi = Retrofit.Builder()
        .baseUrl("http://10.0.0.26:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthApi::class.java)

    suspend fun login(email: String, password: String): AuthUiState {
        return try {
            val resp = api.login(LoginRequest(email.trim(), password))
            AuthUiState.Success(resp.token)
        } catch (e: Exception) {
            AuthUiState.Error(e.message ?: "Erreur réseau")
        }
    }

    suspend fun register(email: String, password: String): AuthUiState {
        return try {
            val resp = api.register(LoginRequest(email.trim(), password))
            AuthUiState.Success(resp.token)
        } catch (e: Exception) {
            AuthUiState.Error(e.message ?: "Erreur réseau")
        }
    }
}
