package com.f4hbw.simulation112.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.f4hbw.simulation112.viewmodel.AuthUiState
import com.f4hbw.simulation112.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToSignup: () -> Unit,
    onNavigateToForgot: () -> Unit,
    onLoginSuccess: (token: String) -> Unit
) {
    val state by viewModel.loginState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Se souvenir de moi
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = {
                    rememberMe = it
                    viewModel.setRememberMe(it)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Se souvenir de moi", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Se connecter")
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToForgot) {
            Text("Mot de passe oublié ?")
        }
        TextButton(onClick = onNavigateToSignup) {
            Text("Créer un compte")
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            AuthUiState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthUiState.Success -> {
                val token = (state as AuthUiState.Success).message
                LaunchedEffect(token) {
                    onLoginSuccess(token)
                }
            }
            is AuthUiState.Error -> {
                Text(
                    text = "Erreur : ${(state as AuthUiState.Error).error}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {
                // Idle
            }
        }
    }
}
