package org.appcenter.inudorm.presentation

import android.content.Context
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.repository.UserRepository
import org.appcenter.inudorm.usecase.Login
import org.appcenter.inudorm.usecase.UserInputParams

class LoginViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {
    private val TAG = "[LoginViewModel]"

    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    fun tryToLoginWithInput() {
        viewModelScope.launch {
            Login(dataStore).run(UserInputParams(email.value!!, password.value!!))
        }
    }
}

class LoginViewModelFactory(private val dataStore: DataStore<Preferences>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}