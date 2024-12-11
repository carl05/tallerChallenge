package com.flicker.myapplication.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val _uiState: MutableStateFlow<LoginUIState> = MutableStateFlow(LoginUIState.Started)
    val uiState = _uiState.asStateFlow()

    fun tryLogin(loginName: String, loginPass:String){
        viewModelScope.launch {
            _uiState.tryEmit(LoginUIState.Loading)
            delay(2000)
            if(loginName.isNotEmpty() && loginPass.isNotEmpty()){
               _uiState.tryEmit(LoginUIState.Sucess(login = loginName, pass = loginPass))
            }else{
                _uiState.tryEmit(LoginUIState.Error)
            }
        }
    }

    sealed class LoginUIState{
        data object Started: LoginUIState()
        data object Loading: LoginUIState()
        data object Error: LoginUIState()
        data class Sucess(
            val login: String,
            val pass: String,
        ): LoginUIState()
    }

}
