package com.example.pengelolakeuangan

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _userState = mutableStateOf(UserState())
    private val _transaksiState = mutableStateOf(TransaksiState())

    val usersState: State<UserState> = _userState
    val transaksiState : State<TransaksiState> = _transaksiState

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

    internal fun fetchTransaksi(){
        viewModelScope.launch {
            try {
                val response = MoneyService.getTransaksi()
                Log.d("RESPONSE", response.toString())
                _transaksiState.value = _transaksiState.value.copy(
                    list = response,
                    loading = false,
                    error = null

                )
            }catch (e:Exception){
                _transaksiState.value = transaksiState.value.copy(
                    loading = false,
                    error = "Error fetching transaksi ${e.message}"
                )
            }
        }
    }

    data class UserState(
        val loading: Boolean = true,
        val list: List<ApiService.User> = emptyList(),
        val error: String? = null
    )
    data class TransaksiState(
        val loading: Boolean = true,
        val list: List<ApiService.Transaksi> = emptyList(),
        val error: String? = null
    )
}