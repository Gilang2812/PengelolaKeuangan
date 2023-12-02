package com.example.pengelolakeuangan

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _userState = mutableStateOf(MoneyState())

    val usersState: State<MoneyState> = _userState

    internal fun fetchUsers(){
        viewModelScope.launch {
            try {
                val response = MoneyService.getUser()
                Log.d("RESPONSE", response.toString())
                _userState.value = _userState.value.copy(
                    list = response,
                    loading = false,
                    error = null

                )
            }catch (e:Exception){
                _userState.value = usersState.value.copy(
                    loading = false,
                    error = "Error  fetching Users ${e.message}"
                )
            }
        }
    }

    data class MoneyState(
        val loading: Boolean = true,
        val list: List<ApiService.User> = emptyList(),
        val error: String? = null
    )
}