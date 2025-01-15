package com.example.grouptasker.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.grouptasker.R
import com.example.grouptasker.data.models.User
import com.example.grouptasker.viewModels.AuthViewModel


@Composable
fun AuthScreen(authViewModel: AuthViewModel) {
    var isRegisterMode by remember { mutableStateOf(false) }

    // Поля для регистрации/входа
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isRegisterMode) "Registration" else "Login",
//            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isRegisterMode) {
            // Поля для регистрации
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        // Поля для входа/регистрации
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        if (isRegisterMode) {
            TextField(
                value = passwordConfirmation,
                onValueChange = { passwordConfirmation = it },
                label = { Text("Confirm Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Button(
            onClick = {
                try {
                    if (isRegisterMode) {
                        val user = User(
                            name = name,
                            lastName = lastName,
                            email = email,
                            password = password,
                            passwordConfirmations = passwordConfirmation
                        )
                        if (user.isValidForCreate()) {

                            try {
                                authViewModel.register(user)
                            } catch (e: Exception) {
                                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill in all required fields for registration.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        try {
                            authViewModel.login(email, password)


                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }


                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            enabled = if (isRegisterMode) {
                name.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() &&
                        password.isNotBlank() && passwordConfirmation.isNotBlank()
            } else {
                email.isNotBlank() && password.isNotBlank()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(if (isRegisterMode) "Register" else "Login")
        }

        // Кнопка переключения между режимами
        TextButton(
            onClick = { isRegisterMode = !isRegisterMode },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRegisterMode) "Switch to Login" else "Switch to Register")
        }
    }
}