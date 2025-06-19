package com.f4hbw.simulation112.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.f4hbw.simulation112.viewmodel.AuthUiState
import com.f4hbw.simulation112.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.forgotState.collectAsState()
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Mot de passe oublié", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.forgotPassword(email) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Envoyer email")
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onNavigateBack) { Text("Retour") }
        Spacer(Modifier.height(16.dp))
        when (state) {
            AuthUiState.Loading -> CircularProgressIndicator()
            is AuthUiState.Success -> Text("Succès: ${(state as AuthUiState.Success).message}")
            is AuthUiState.Error -> Text("Erreur: ${(state as AuthUiState.Error).error}", color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }
}
