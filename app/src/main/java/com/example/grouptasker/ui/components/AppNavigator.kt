package com.example.grouptasker.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.grouptasker.viewModels.AuthState
import com.example.grouptasker.viewModels.AuthStateManager
import com.example.grouptasker.viewModels.AuthViewModel
import com.example.grouptasker.viewModels.GroupViewModel
import com.example.grouptasker.viewModels.TaskViewModel
import com.example.grouptasker.viewModels.UserViewModel


@Composable
fun AppNavigator(
    groupViewModel: GroupViewModel,
    taskViewModel: TaskViewModel,
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel
) {

    val navController = rememberNavController()

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState !is AuthState.LoggedIn) {
            navController.navigate("auth") {
                popUpTo(0)
            }
        }
    }


    NavHost(navController = navController, startDestination = getStartDestination(authState)) {

        composable("auth") {
            AuthScreen(authViewModel)
        }
        composable("main") {
            MainScreen(
                groupViewModel: GroupViewModel,
                taskViewModel: TaskViewModel,
                userViewModel: UserViewModel
            )
        }
    }
}

private fun getStartDestination(authState: AuthState): String {
    return if (authState is AuthState.LoggedIn) "main" else "auth"
}