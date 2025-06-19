package com.f4hbw.simulation112.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.f4hbw.simulation112.model.*
import com.f4hbw.simulation112.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val message: String) : AuthUiState()
    data class Error(val error: String) : AuthUiState()
}

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    // SharedPreferences pour le "Se souvenir de moi"
    private val prefs = app.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private var rememberMeFlag: Boolean = false

    // États pour l'authentification
    private val _signupState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val signupState: StateFlow<AuthUiState> = _signupState

    private val _loginState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val loginState: StateFlow<AuthUiState> = _loginState

    private val _forgotState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val forgotState: StateFlow<AuthUiState> = _forgotState

    private val _resetState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val resetState: StateFlow<AuthUiState> = _resetState

    // Jeton et pseudo
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _userPseudo = MutableStateFlow<String>("")
    val userPseudo: StateFlow<String> = _userPseudo

    init {
        // Charger le token si on a coché "Se souvenir de moi"
        val savedToken = prefs.getString("saved_token", null)
        if (savedToken != null) {
            _token.value = savedToken
            decodeAndSetPseudo(savedToken)
        }
    }

    /** Définir le flag "se souvenir de moi" avant login */
    fun setRememberMe(remember: Boolean) {
        rememberMeFlag = remember
    }

    private fun saveTokenIfNeeded(jwtToken: String) {
        if (rememberMeFlag) {
            prefs.edit()
                .putString("saved_token", jwtToken)
                .apply()
        } else {
            prefs.edit()
                .remove("saved_token")
                .apply()
        }
    }

    /** Décoder le JWT pour extraire le pseudo */
    private fun decodeAndSetPseudo(jwtToken: String) {
        try {
            val jwt = JWT(jwtToken)
            _userPseudo.value = jwt.getClaim("pseudo").asString() ?: ""
        } catch (e: Exception) {
            _userPseudo.value = ""
        }
    }

    fun signup(pseudo: String, email: String, password: String) = viewModelScope.launch {
        _signupState.value = AuthUiState.Loading
        try {
            val resp = RetrofitInstance.api.signup(SignupRequest(pseudo, email, password))
            if (resp.isSuccessful) {
                _signupState.value = AuthUiState.Success(resp.body()?.message ?: "Inscription réussie")
            } else {
                _signupState.value = AuthUiState.Error(resp.errorBody()?.string() ?: "Erreur inscription")
            }
        } catch (e: Exception) {
            _signupState.value = AuthUiState.Error(e.localizedMessage ?: "Erreur réseau")
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginState.value = AuthUiState.Loading
        try {
            val resp = RetrofitInstance.api.login(LoginRequest(email, password))
            if (resp.isSuccessful) {
                val jwtToken = resp.body()?.token ?: ""
                // Sauvegarder et décoder
                _loginState.value = AuthUiState.Success(jwtToken)
                _token.value = jwtToken
                saveTokenIfNeeded(jwtToken)
                decodeAndSetPseudo(jwtToken)
            } else {
                _loginState.value = AuthUiState.Error(resp.errorBody()?.string() ?: "Erreur login")
            }
        } catch (e: Exception) {
            _loginState.value = AuthUiState.Error(e.localizedMessage ?: "Erreur réseau")
        }
    }

    fun forgotPassword(email: String) = viewModelScope.launch {
        _forgotState.value = AuthUiState.Loading
        try {
            val resp = RetrofitInstance.api.forgotPassword(ForgotPasswordRequest(email))
            if (resp.isSuccessful) {
                _forgotState.value = AuthUiState.Success(resp.body()?.message ?: "Email envoyé")
            } else {
                _forgotState.value = AuthUiState.Error(resp.errorBody()?.string() ?: "Erreur reset")
            }
        } catch (e: Exception) {
            _forgotState.value = AuthUiState.Error(e.localizedMessage ?: "Erreur réseau")
        }
    }

    fun resetPassword(token: String, newPassword: String) = viewModelScope.launch {
        _resetState.value = AuthUiState.Loading
        try {
            val resp = RetrofitInstance.api.resetPassword(ResetPasswordRequest(token, newPassword))
            if (resp.isSuccessful) {
                _resetState.value = AuthUiState.Success(resp.body()?.message ?: "Mot de passe réinitialisé")
            } else {
                _resetState.value = AuthUiState.Error(resp.errorBody()?.string() ?: "Erreur reset")
            }
        } catch (e: Exception) {
            _resetState.value = AuthUiState.Error(e.localizedMessage ?: "Erreur réseau")
        }
    }

    /** Efface le token (logout) */
    fun clearToken() {
        prefs.edit().remove("saved_token").apply()
        _token.value = null
        _userPseudo.value = ""
    }
}
