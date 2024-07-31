package com.ucopdesoft.issuelogger.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.login__authentication.ComplaintListCallBack
import com.ucopdesoft.issuelogger.repositories.ComplaintsRepository
import com.ucopdesoft.issuelogger.utils.NetworkCheck
import com.ucopdesoft.issuelogger.utils.NetworkStatus
import com.ucopdesoft.issuelogger.utils.ToastMessage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ComplaintsViewModel(application: Application) : AndroidViewModel(application) {

    private var complaintsRepository = ComplaintsRepository(application.applicationContext)
    private var connectionLiveData = NetworkCheck(application.applicationContext)

    val statusMessage = complaintsRepository.message

    val counter = complaintsRepository.counter

    private var _uri = MutableLiveData<ArrayList<Uri>?>()
    val uri: LiveData<ArrayList<Uri>?> = _uri

    private var _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private var _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private var _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private var _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private var _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> = _latitude

    private var _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> = _longitude


    private fun registerNewComplaint(
        context: Context,
        complaintId: String?,
        complaintTitle: String,
        complaintDescription: String,
        address: String,
        latitude: Double,
        longitude: Double,
        date: String
    ) {
        viewModelScope.launch {
            complaintsRepository.registerComplaint(
                context,
                complaintId,
                complaintTitle,
                complaintDescription,
                address,
                latitude,
                longitude,
                date
            )
        }
    }

    fun getAllComplaints(
        context: Context,
        listener: ComplaintListCallBack
    ) {
        if (connectionLiveData.isOnline() == NetworkStatus.CONNECTED) {
            viewModelScope.launch {
                complaintsRepository.getAllComplaints(context, listener)
            }
        } else {
            ToastMessage.showMessage(
                context,
                context.resources.getString(R.string.pleasecheckinternet)
            )
        }
    }

    fun getComplaintDetails(
        context: Context,
        complaintId: String,
        listener: ComplaintListCallBack
    ) {
        if (connectionLiveData.isOnline() == NetworkStatus.CONNECTED) {
            viewModelScope.launch {
                complaintsRepository.getComplaintDetails(context, complaintId, listener)
//                if (visibility == View.VISIBLE) {
//                    complaintsRepository.getComplaintImages(context, complaintId, listener)
//                }
            }
        } else {
            ToastMessage.showMessage(
                context,
                context.resources.getString(R.string.pleasecheckinternet)
            )
        }
    }

    fun getComplaintsImages(
        context: Context,
        complaintId: String,
        listener: ComplaintListCallBack
    ) {
        if (connectionLiveData.isOnline() == NetworkStatus.CONNECTED) {
            viewModelScope.launch {
                complaintsRepository.getComplaintImages(context, complaintId, listener)
            }
        } else {
            ToastMessage.showMessage(
                context,
                context.resources.getString(R.string.pleasecheckinternet)
            )
        }
    }

    fun getComplaintsComments(
        context: Context,
        complaintId: String,
        listener: ComplaintListCallBack,
    ) {
        viewModelScope.launch {
            complaintsRepository.getComments(context, complaintId, listener)
        }
    }

    fun getCurrentLocation(context: Context, listener: ComplaintListCallBack) {
        if (connectionLiveData.isOnline() == NetworkStatus.CONNECTED) {
            viewModelScope.launch {
                complaintsRepository.getCurrentLocation(context, listener)
            }
        } else {
            ToastMessage.showMessage(
                context,
                context.resources.getString(R.string.pleasecheckinternet)
            )
        }
    }

    fun getTime(): String = SimpleDateFormat(
        "d MMMM, yyyy K:mm aa",
        Locale.getDefault()
    ).format(Calendar.getInstance().time)

    fun setImagesUris(uri: ArrayList<Uri>?) {
        _uri.value = uri
    }

    fun setDescriptionURI(
        description: String,
        uri: ArrayList<Uri>,
        title: String,
        address: String,
        latitude: Double,
        longitude: Double,
        date: String
    ) {
        _uri.value = uri
        _description.value = description
        _title.value = title
        _address.value = address
        _latitude.value = latitude
        _longitude.value = longitude
        _date.value = date
    }

    private fun storeImagesOnFirebase(context: Context) {
        viewModelScope.launch {
            complaintsRepository.storeImagesOnFirebase(context, uri.value!!)
        }
    }

    fun registerComplaint(context: Context, complaintId: String? = null) {

        if (!_title.value.isNullOrEmpty() || !_description.value.isNullOrEmpty()) {

            if (!_uri.value.isNullOrEmpty()) {
                storeImagesOnFirebase(context)
            } else {
                registerNewComplaint(
                    context,
                    complaintId,
                    _title.value!!,
                    _description.value!!,
                    _address.value!!,
                    _latitude.value!!,
                    _longitude.value!!,
                    _date.value!!

                )
            }
        } else {
            ToastMessage.showMessage(
                context,
                context.resources.getString(R.string.please_enter_title_or_description)
            )
        }
    }

    fun setComment(context: Context, complaintId: String, comment: String) {
        viewModelScope.launch {
            complaintsRepository.setComment(context, complaintId, comment)
        }
    }
}