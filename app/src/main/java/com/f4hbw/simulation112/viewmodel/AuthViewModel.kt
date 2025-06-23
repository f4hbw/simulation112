package com.f4hbw.simulation112.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.f4hbw.simulation112.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Représente l’état UI des appels d’authentification.
 */
sealed class AuthUiState {
    object Loading : AuthUiState()
    data class Success(val message: String) : AuthUiState()
    data class Error(val error: String) : AuthUiState()
}

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {
    // État du dernier appel de login/signup/etc.
    private val _loginState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val loginState: StateFlow<AuthUiState> = _loginState

    // Jeton JWT actuel (null si non connecté)
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    // Pseudo extrait du JWT après connexion
    private val _userPseudo = MutableStateFlow("")
    val userPseudo: StateFlow<String> = _userPseudo

    /**
     * Lance la requête de connexion.
     * En cas de succès, stocke le token et décode le claim "pseudo".
     */
    fun login(email: String, password: String) = viewModelScope.launch {
        _loginState.value = AuthUiState.Loading
        when (val result = repo.login(email, password)) {
            is AuthUiState.Success -> {
                val jwtToken = result.message
                _loginState.value = AuthUiState.Success(jwtToken)
                _token.value = jwtToken

                // Décodage du JWT pour récupérer le pseudo
                val jwt = JWT(jwtToken)
                jwt.getClaim("pseudo").asString()?.let { pseudo ->
                    _userPseudo.value = pseudo
                }
            }
            is AuthUiState.Error -> {
                _loginState.value = result
            }
            else -> { /* pas utilisé ici */ }
        }
    }

    /** Vide le token et le pseudo (déconnexion) */
    fun clearToken() {
        _token.value = null
        _userPseudo.value = ""
    }

    // Vous pouvez ajouter ici signup(), resetPassword(), etc. selon vos besoins
}
