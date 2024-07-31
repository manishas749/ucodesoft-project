package com.ucopdesoft.issuelogger.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucopdesoft.issuelogger.models.User
import com.ucopdesoft.issuelogger.utils.Constants.Companion.USER_NAME
import com.ucopdesoft.issuelogger.utils.Constants.Companion.USER_NUMBER
import com.ucopdesoft.issuelogger.utils.Constants.Companion.USER_PROFILE_PIC
import com.ucopdesoft.issuelogger.utils.Tables
import com.ucopdesoft.issuelogger.utils.ToastMessage

class UsersRepository(context: Context) {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    val userList = MutableLiveData<List<Pair<String, User>>>()

    fun getAllUsers(context: Context) {
        firebaseDatabase.reference.child(Tables.USER.tableName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = arrayListOf<Pair<String, User>>()

                    for (user in snapshot.children) {

                        val userData = user.value as Map<*, *>

                        val userName = userData[USER_NAME].toString()
                        val userNumber = userData[USER_NUMBER].toString()
                        //val profilePic = userData[USER_PROFILE_PIC].toString()

                        val u = User(userName, userNumber)

                        val pair = Pair(user.key!!, u)

                        list.add(pair)
                    }

                    userList.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    ToastMessage.showMessage(context, error.message)
                }
            })
    }
}