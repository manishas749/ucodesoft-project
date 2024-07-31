package com.ucopdesoft.issuelogger.repositories

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.ucopdesoft.issuelogger.listeners.ComplaintsListener
import com.ucopdesoft.issuelogger.login__authentication.ComplaintListCallBack
import com.ucopdesoft.issuelogger.models.Comment
import com.ucopdesoft.issuelogger.models.Complaint
import com.ucopdesoft.issuelogger.utils.ComplaintStatus
import com.ucopdesoft.issuelogger.utils.Constants
import com.ucopdesoft.issuelogger.utils.Constants.Companion.ADDRESS
import com.ucopdesoft.issuelogger.utils.Constants.Companion.COMMENT
import com.ucopdesoft.issuelogger.utils.Constants.Companion.COMPLAINT_DESCRIPTION
import com.ucopdesoft.issuelogger.utils.Constants.Companion.COMPLAINT_ID
import com.ucopdesoft.issuelogger.utils.Constants.Companion.COMPLAINT_STATUS
import com.ucopdesoft.issuelogger.utils.Constants.Companion.COMPLAINT_TITLE
import com.ucopdesoft.issuelogger.utils.Constants.Companion.DATE
import com.ucopdesoft.issuelogger.utils.Constants.Companion.DOWN_VOTE
import com.ucopdesoft.issuelogger.utils.Constants.Companion.LATITUDE
import com.ucopdesoft.issuelogger.utils.Constants.Companion.LONGITUDE
import com.ucopdesoft.issuelogger.utils.Constants.Companion.UP_VOTE
import com.ucopdesoft.issuelogger.utils.Constants.Companion.USER_ID
import com.ucopdesoft.issuelogger.utils.Tables
import com.ucopdesoft.issuelogger.utils.ToastMessage
import com.ucopdesoft.issuelogger.utils.UserDetailsPreference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ComplaintsRepository(context: Context) : ComplaintsListener {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private val userDetailsPreference = UserDetailsPreference(context)
    private var id: String? = userDetailsPreference.getToken()
    private val statusMessage = MutableLiveData<Boolean>()
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val message: LiveData<Boolean>
        get() = statusMessage

    private val _counter = MutableLiveData<Pair<String, Int>>()
    val counter: LiveData<Pair<String, Int>>
        get() = _counter

    override suspend fun registerComplaint(
        context: Context,
        complaintId: String?,
        complaintTitle: String,
        complaintDescription: String,
        address: String,
        latitude: Double,
        longitude: Double,
        date: String
    ) {
        if (complaintId != null) {
            updateOnDB(
                complaintId,
                id!!,
                complaintTitle,
                address,
                latitude,
                longitude,
                complaintDescription,
                Calendar.getInstance().time.time
            )
        } else {
            val currentTime = SimpleDateFormat(
                Constants.COMPLAINT_ID_FORMAT, Locale.getDefault()
            ).format(Calendar.getInstance().time)

            val complaintId2 = currentTime.plus(id)
            updateOnDB(
                complaintId2,
                id!!,
                complaintTitle,
                address,
                latitude,
                longitude,
                complaintDescription,
                Calendar.getInstance().time.time
            )
        }
    }

    override suspend fun storeImagesOnFirebase(context: Context, imagesUri: ArrayList<Uri>) {
        if (imagesUri.isNotEmpty()) {
            val currentTime = SimpleDateFormat(
                Constants.COMPLAINT_ID_FORMAT, Locale.getDefault()
            ).format(Calendar.getInstance().time)

            val complaintId = currentTime.plus(id)

            for (count in 0 until imagesUri.size) {
                storeOnFirebase(context, complaintId, count, imagesUri[count])
            }
        }
    }

    private fun storeOnFirebase(
        context: Context, complaintId: String, count: Int, singleImageUri: Uri
    ) {
        val storageRef =
            firebaseStorage.reference.child(complaintId).child(complaintId).child(count.toString())
        storageRef.putFile(singleImageUri).addOnCompleteListener { putFile ->
            if (putFile.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { downloadUrl ->
                    if (downloadUrl.isSuccessful) {
                        firebaseDatabase.reference.child(Tables.IMAGES.tableName).child(complaintId)
                            .child((count).toString()).setValue(downloadUrl.result.toString())

                        _counter.postValue(Pair(complaintId, count+1))
                    } else {
                        ToastMessage.showMessage(context,downloadUrl.exception!!.message.toString())
                    }
                }

            } else {
                ToastMessage.showMessage(context, putFile.exception!!.message.toString())
                statusMessage.postValue(false)
            }
        }

    }

    private fun updateOnDB(
        complaintId: String,
        userId: String,
        complaintTitle: String,
        address: String,
        latitude: Double,
        longitude: Double,
        complaintDescription: String,
        date: Long
    ) {
        val complaint = Complaint(
            complaintId = complaintId,
            userId = userId,
            complaintTitle = complaintTitle,
            address = address,
            latitude = latitude,
            longitude = longitude,
            complaintDescription = complaintDescription,
            upVote = 0,
            downVote = 0,
            status = ComplaintStatus.ACTIVE.status,
            date = date
        )
        firebaseDatabase.reference.child(Tables.COMPLAINTS.tableName).child(complaintId)
            .setValue(complaint).addOnCompleteListener {
                statusMessage.value = it.isSuccessful
            }
    }

    override suspend fun getAllComplaints(
        context: Context, listener: ComplaintListCallBack
    ) {
        super.getAllComplaints(context, listener)

        if (id != null) {
            firebaseDatabase.reference.child(Tables.COMPLAINTS.tableName)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val list = arrayListOf<Complaint>()

                        for (complaints in snapshot.children) {

                            val complaintMap = complaints.value as Map<*, *>

                            val complaint = Complaint(
                                complaintId = complaintMap[COMPLAINT_ID].toString(),
                                userId = complaintMap[USER_ID].toString(),
                                complaintTitle = complaintMap[COMPLAINT_TITLE].toString(),
                                complaintDescription = complaintMap[COMPLAINT_DESCRIPTION].toString(),
                                address = complaintMap[ADDRESS].toString(),
                                latitude = if (complaintMap[LATITUDE].toString() != "null") complaintMap[LATITUDE].toString()
                                    .toDouble() else 0.0,
                                longitude = if (complaintMap[LONGITUDE].toString() != "null") complaintMap[LONGITUDE].toString()
                                    .toDouble() else 0.0,
                                upVote = if (complaintMap[UP_VOTE].toString() != "null") complaintMap[UP_VOTE].toString()
                                    .toInt() else 0,
                                downVote = if (complaintMap[DOWN_VOTE].toString() != "null") complaintMap[DOWN_VOTE].toString()
                                    .toInt() else 0,
                                status = complaintMap[COMPLAINT_STATUS].toString(),
                                date = if (complaintMap[DATE].toString() != "null") complaintMap[DATE].toString()
                                    .toLong() else 0L
                            )

                            list.add(complaint)
                        }
                        list.sortByDescending { it.complaintId }
                        listener.onResponse(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        listener.onResponse(mutableListOf())
                    }
                })
        }
    }

    override suspend fun getComplaintDetails(
        context: Context, complaintId: String, listener: ComplaintListCallBack
    ) {
        super.getComplaintDetails(context, complaintId, listener)
        firebaseDatabase.reference.child(Tables.COMPLAINTS.tableName).child(complaintId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val complaintMap = snapshot.value as Map<*, *>

                    val complaint = Complaint(
                        complaintId = complaintMap[COMPLAINT_ID].toString(),
                        userId = complaintMap[USER_ID].toString(),
                        complaintTitle = complaintMap[COMPLAINT_TITLE].toString(),
                        complaintDescription = complaintMap[COMPLAINT_DESCRIPTION].toString(),
                        address = complaintMap[ADDRESS].toString(),
                        latitude = if (complaintMap[LATITUDE].toString() != "null") complaintMap[LATITUDE].toString()
                            .toDouble() else 0.0,
                        longitude = if (complaintMap[LONGITUDE].toString() != "null") complaintMap[LONGITUDE].toString()
                            .toDouble() else 0.0,
                        upVote = if (complaintMap[UP_VOTE].toString() != "null") complaintMap[UP_VOTE].toString()
                            .toInt() else 0,
                        downVote = if (complaintMap[DOWN_VOTE].toString() != "null") complaintMap[DOWN_VOTE].toString()
                            .toInt() else 0,
                        status = complaintMap[COMPLAINT_STATUS].toString(),
                        date = if (complaintMap[DATE].toString() != "null") complaintMap[DATE].toString()
                            .toLong() else 0L

                    )

                    listener.onComplaintDetailResponse(complaint)

                }

                override fun onCancelled(error: DatabaseError) {
                    listener.onComplaintDetailResponse(null)
                }
            })
    }

    override suspend fun getComplaintImages(
        context: Context, complaintId: String, listener: ComplaintListCallBack
    ) {
        super.getComplaintImages(context, complaintId, listener)

        firebaseDatabase.reference.child(Tables.IMAGES.tableName).child(complaintId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = arrayListOf<String>()

                    for (image in snapshot.children) {
                        list.add(image.value.toString())
                    }

                    listener.onComplaintImagesResponse(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    listener.onComplaintImagesResponse(null)
                }
            })
    }

    override suspend fun setComment(
        context: Context, complaintId: String, comment: String
    ) {
        super.setComment(context, complaintId, comment)

        val commentId = "COM".plus(
            SimpleDateFormat(
                Constants.COMPLAINT_ID_FORMAT, Locale.getDefault()
            ).format(Calendar.getInstance().time)
        ).plus(id!!)

        val com = Comment(complaintId, id!!, comment, Calendar.getInstance().time.time)
        firebaseDatabase.reference.child(Tables.COMMENTS.tableName).child(complaintId)
            .child(commentId)
            .setValue(com)
    }

    override suspend fun getComments(
        context: Context, complaintId: String, listener: ComplaintListCallBack
    ) {
        super.getComments(context, complaintId, listener)

        firebaseDatabase.reference.child(Tables.COMMENTS.tableName).child(complaintId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<Comment>()

                    for (comment in snapshot.children) {
                        val commentMap = comment.value as Map<*, *>

                        val comm = Comment(
                            complaintId = commentMap[COMPLAINT_ID].toString(),
                            userId = commentMap[USER_ID].toString(),
                            comment = commentMap[COMMENT].toString(),
                            date = if (commentMap[DATE].toString() != "null") commentMap[DATE].toString()
                                .toLong() else 0L
                        )
                        list.add(comm)
                    }

                    list.sortByDescending { it.date }

                    listener.onComplaintCommentsResponse(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    listener.onComplaintCommentsResponse(null)
                }
            })
    }

    override suspend fun getCurrentLocation(context: Context, listener: ComplaintListCallBack) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (context as Activity), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 101
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation

        task.addOnSuccessListener { it ->
            if (it != null) {
                val geo = Geocoder(context)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geo.getFromLocation(it.latitude, it.longitude, 1) {
                        listener.onResponseLocation(it)
                    }
                }
            }
        }
    }
}