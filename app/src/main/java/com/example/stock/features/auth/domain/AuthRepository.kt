package com.example.stock.features.auth.domain

import com.example.stock.util.Preferences
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    suspend fun signInUser(user: User): AuthState {
        try {
            supabaseClient.auth.signInWith(Email) {
                email = user.email.trim()
                password = user.password.trim()
            }
            saveToken()
            return AuthState.Success("Login was successful!")
        } catch (e: Exception) {
            return AuthState.Error("Something went wrong, Try Again!")
        }
    }

    suspend fun signUpUser(user: User): AuthState {
        try {
            supabaseClient.auth.signUpWith(Email) {
                email = user.email.trim()
                password = user.password.trim()
            }
            saveToken()
            return AuthState.Success("Registered successfully!")
        } catch (e: Exception) {
            return AuthState.Error("Something went wrong, Try Again!")
        }
    }

    private fun saveToken() {
        val accessToken = supabaseClient.auth.currentAccessTokenOrNull()
        Preferences.setAccessToken(accessToken.toString())
    }

    private fun removeToken() {
        Preferences.removeAccessToken()
    }

    suspend fun signOut(): AuthState {
        try {
            supabaseClient.auth.signOut()
            removeToken()
            return AuthState.Success("Sign Out successfully!")
        } catch (e: Exception) {
            return AuthState.Error("Something went wrong, Try Again!")
        }
    }
}
