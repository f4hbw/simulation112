// File: app/src/main/java/com/f4hbw/simulation112/ui/auth/ResetPasswordScreen.kt
package com.f4hbw.simulation112.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.f4hbw.simulation112.viewmodel.AuthUiState
import com.f4hbw.simulation112.viewmodel.AuthViewModel

@Composable
fun ResetPasswordScreen(
    viewModel: AuthViewModel,
    token: String,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.resetState.collectAsState()
    var newPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Réinitialiser mot de passe", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Nouveau mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.resetPassword(token, newPassword) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Valider")
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onNavigateBack) { Text("Retour") }
        Spacer(Modifier.height(16.dp))
        when (state) {
            AuthUiState.Loading -> CircularProgressIndicator()
            is AuthUiState.Success -> Text("Succès: ${(state as AuthUiState.Success).message}")
            is AuthUiState.Error -> Text(
                "Erreur: ${(state as AuthUiState.Error).error}",
                color = MaterialTheme.colorScheme.error
            )
            else -> {}
        }
    }
}
