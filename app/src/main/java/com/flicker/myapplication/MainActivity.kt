package com.flicker.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.flicker.myapplication.ui.theme.LoginViewModel
import com.flicker.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: LoginViewModel by viewModels()
                val uiState = viewModel.uiState.collectAsState()
                var loginName by remember { mutableStateOf("") }
                var loginPassword by remember { mutableStateOf("") }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Greeting(
                            modifier = Modifier.padding(innerPadding)
                        )
                        TextField(
                            value = loginName,
                            label = { Text("Login:") },
                            onValueChange = {
                                loginName = it
                            }
                        )

                        TextField(
                            value = loginPassword,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            label = { Text("Password:") },
                            onValueChange = {
                                loginPassword = it
                            }
                        )

                        Button(
                            content = {
                                if (uiState.value is LoginViewModel.LoginUIState.Loading) {
                                    CircularProgressIndicator(
                                        color = Color.Black
                                    )
                                } else {
                                    Text("login")
                                }
                            },
                            onClick = {
                                viewModel.tryLogin(loginName = loginName, loginPass = loginPassword)
                            }
                        )

                        when (uiState.value) {
                            LoginViewModel.LoginUIState.Error -> {
                                Text(
                                    text = "Invalid login"
                                )
                            }

                            is LoginViewModel.LoginUIState.Sucess -> {
                                Text(
                                    text = "Success Login"
                                )
//                                saveUserCredentials(uiState.value)
                            }

                            LoginViewModel.LoginUIState.Loading,
                            LoginViewModel.LoginUIState.Started -> {
                            }
                        }


                    }
                }
            }
        }
    }

    fun saveUserCredentials(
        state: LoginViewModel.LoginUIState.Sucess
    ){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(state.login, "login")
            putString(state.pass, "pass")
            apply()
        }
    }
    fun loadUserCredentials(
    ):LoginViewModel.LoginUIState.Sucess{
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val login = sharedPref.getString("login", "") ?: ""
        val pass = sharedPref.getString("pass", "") ?: ""
        return LoginViewModel.LoginUIState.Sucess(login, pass)
    }
}



@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "Login Page",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting()
    }
}