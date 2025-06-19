package com.f4hbw.simulation112.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.f4hbw.simulation112.ui.auth.ForgotPasswordScreen
import com.f4hbw.simulation112.ui.auth.LoginScreen
import com.f4hbw.simulation112.ui.auth.ResetPasswordScreen
import com.f4hbw.simulation112.ui.auth.SignupScreen
import com.f4hbw.simulation112.ui.home.HomeScreen
import com.f4hbw.simulation112.ui.scenario.ScenarioScreen
import com.f4hbw.simulation112.viewmodel.AuthViewModel
import com.f4hbw.simulation112.viewmodel.ScenarioViewModel

sealed class Screen(val route: String) {
    object Login    : Screen("login")
    object Signup   : Screen("signup")
    object Forgot   : Screen("forgot")
    object Reset    : Screen("reset/{token}") {
        fun createRoute(token: String) = "reset/$token"
    }
    object Home     : Screen("home")
    object Scenario : Screen("scenario")
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val authVm: AuthViewModel = viewModel()
    val scenarioVm: ScenarioViewModel = viewModel()
    val token by authVm.token.collectAsState()

    val startRoute = if (token.isNullOrEmpty()) Screen.Login.route else Screen.Home.route

    NavHost(navController = navController, startDestination = startRoute) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel          = authVm,
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) },
                onNavigateToForgot = { navController.navigate(Screen.Forgot.route) },
                onLoginSuccess     = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Signup.route) {
            SignupScreen(viewModel = authVm, onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Forgot.route) {
            ForgotPasswordScreen(viewModel = authVm, onNavigateBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.Reset.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val tokenArg = backStackEntry.arguments?.getString("token") ?: ""
            ResetPasswordScreen(
                viewModel      = authVm,
                token          = tokenArg,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Home.route) {
            val pseudo by authVm.userPseudo.collectAsState()
            HomeScreen(
                pseudo          = pseudo,
                onLogout        = {
                    authVm.clearToken()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onScenarioClick = {
                    navController.navigate(Screen.Scenario.route)
                }
            )
        }
        composable(Screen.Scenario.route) {
            ScenarioScreen(viewModel = scenarioVm)
        }
    }
}
