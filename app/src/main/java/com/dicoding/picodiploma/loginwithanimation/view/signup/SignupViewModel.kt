package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.di.ResultState
import com.google.gson.Gson
import retrofit2.HttpException

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    private val registerResponse = MutableLiveData<RegisterResponse>()
    private val registrationResult: LiveData<RegisterResponse>
        get() = registerResponse

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            //get success message
            val message = repository.register(name, email, password).message
            emit(ResultState.Success(message))
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            emit(ResultState.Error(errorBody.message.toString()))
        }
    }
}