package com.ucopdesoft.issuelogger.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ucopdesoft.issuelogger.models.User
import com.ucopdesoft.issuelogger.repositories.UsersRepository
import kotlinx.coroutines.launch

class UsersViewModel(application: Application) : AndroidViewModel(application) {

    private val usersRepository: UsersRepository

    val userList: LiveData<List<Pair<String, User>>>
        get() = usersRepository.userList

    init {
        usersRepository = UsersRepository(application.applicationContext)
    }

    fun getAllUsers(context: Context) {
        viewModelScope.launch {
            usersRepository.getAllUsers(context)
        }
    }
}