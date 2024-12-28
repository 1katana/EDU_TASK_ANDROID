package com.example.grouptasker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.collection.emptyIntSet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateViewModelFactory
import com.example.grouptasker.data.remoteAPI.AuthApi

import com.example.grouptasker.data.models.User
import com.example.grouptasker.ui.theme.GroupTaskerTheme
import com.example.grouptasker.viewModels.AuthState
import com.example.grouptasker.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val authViewModel: AuthViewModel by viewModels()

        enableEdgeToEdge()
        setContent {
            GroupTaskerTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    TestRetrofit(innerPadding,authApi)
//                }
                AuthScreen(viewModel = authViewModel)
            }
        }
    }
}

@Composable
fun AuthScreen(viewModel: AuthViewModel) {
    val authState by viewModel.authState.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(modifier = Modifier.padding(20.dp), onClick = {
            viewModel.login(email = "ivan@2004", password = "12345")
        }) {

        }

        when (authState) {
            is AuthState.Idle -> {

            }

            is AuthState.Loading -> {
                CircularProgressIndicator()
            }

            is AuthState.Success -> {
                val response = (authState as AuthState.Success).response
                Text("Welcome, ${response.email}")
            }

            is AuthState.Error -> {
                val message = (authState as AuthState.Error).message
                Text("Error: $message")
            }

            is AuthState.LoggedOut -> {
                Text("Logged out")
            }
        }
    }
}


@Composable
fun TestRetrofit(innerPadding: PaddingValues, authApi: AuthApi) {

    val userDto = User(
        name = "John",
        lastName = "Doe",
        email = "jo@.com",
        password = "password123",
        passwordConfirmations = "password123"
    )

    Column(
        Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(onClick = {
            authApi.register(userDto).enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val registeredUser = response.body()
                        Log.d("my", "Registration successful: $registeredUser")
                    } else {
                        Log.d("my", "Registration failed: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("my", "Registration request failed: ${t.message}")
                }

            }


            )

        }) { Text("Нажми тварь") }


    }

}


