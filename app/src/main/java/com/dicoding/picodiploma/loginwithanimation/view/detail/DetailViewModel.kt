package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    fun getDetailUser(id: String) = repository.getDetailUser(id)

}