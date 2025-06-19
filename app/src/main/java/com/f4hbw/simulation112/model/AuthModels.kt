package com.f4hbw.simulation112.model

data class SignupRequest(val pseudo: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class ForgotPasswordRequest(val email: String)
data class ResetPasswordRequest(val token: String, val newPassword: String)

data class ApiMessageResponse(val message: String)
data class LoginResponse(val token: String)
