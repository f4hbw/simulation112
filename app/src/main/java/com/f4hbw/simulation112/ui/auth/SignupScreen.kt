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
fun SignupScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.signupState.collectAsState()
    var pseudo by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Création de compte", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = pseudo,
            onValueChange = { pseudo = it },
            label = { Text("Pseudo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.signup(pseudo, email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("S'inscrire")
        }
        Spacer(Modifier.height(8.dp))

        TextButton(onClick = onNavigateBack) {
            Text("Retour")
        }
        Spacer(Modifier.height(16.dp))

        when (state) {
            AuthUiState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthUiState.Success -> {
                Text(
                    text = "Succès : ${(state as AuthUiState.Success).message}",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            is AuthUiState.Error -> {
                Text(
                    text = "Erreur : ${(state as AuthUiState.Error).error}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> { /* Idle */ }
        }
    }
}
