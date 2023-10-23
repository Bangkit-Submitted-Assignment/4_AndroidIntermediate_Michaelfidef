package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SignupViewModel (private val repository: UserRepository): ViewModel() {
    private val registerResponse = MutableLiveData<RegisterResponse>()
    private val registrationResult: LiveData<RegisterResponse>
        get() = registerResponse

    fun getRegisterResponse(): LiveData<RegisterResponse> {
        return registerResponse
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(name, email, password)
                registerResponse.value = response

                if (!response.error) {
                    Log.d("Registration", "Success: ${response.message}")
                } else {
                    Log.e("Registration", "Error: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("Registration", "Error: ${e.message}")
            }
        }
    }
}