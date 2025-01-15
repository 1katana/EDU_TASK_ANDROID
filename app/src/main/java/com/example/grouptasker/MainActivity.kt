package com.example.grouptasker

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.grouptasker.ui.components.AppNavigator
import com.example.grouptasker.ui.theme.GroupTaskerTheme
import com.example.grouptasker.viewModels.AuthStateManager
import com.example.grouptasker.viewModels.AuthViewModel
import com.example.grouptasker.viewModels.TaskViewModel
import com.example.grouptasker.viewModels.UserViewModel
import com.example.grouptasker.viewModels.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val groupViewModel: GroupViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            enableEdgeToEdge()
            AppNavigator(
                groupViewModel = groupViewModel,
                taskViewModel = taskViewModel,
                userViewModel = userViewModel,
                authViewModel = authViewModel

            )
        }
    }
}


