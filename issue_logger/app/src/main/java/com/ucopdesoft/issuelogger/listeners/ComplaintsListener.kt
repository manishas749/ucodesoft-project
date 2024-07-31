package com.ucopdesoft.issuelogger.listeners

import android.content.Context
import android.net.Uri
import com.ucopdesoft.issuelogger.login__authentication.ComplaintListCallBack

interface ComplaintsListener {

    suspend fun registerComplaint(
        context: Context,
        complaintId: String? = null,
        complaintTitle: String,
        complaintDescription: String,
        address: String,
        latitude: Double,
        longitude: Double,
        date: String
    ) {
    }

    suspend fun storeImagesOnFirebase(context: Context, imagesUri: ArrayList<Uri>) {}

    suspend fun getAllComplaints(context: Context, listener: ComplaintListCallBack) {}

    suspend fun getComplaintDetails(
        context: Context, complaintId: String, listener: ComplaintListCallBack
    ) {
    }

    suspend fun getComplaintImages(
        context: Context, complaintId: String, listener: ComplaintListCallBack
    ) {
    }

    suspend fun setComment(
        context: Context, complaintId: String, comment: String
    ) {
    }

    suspend fun getComments(
        context: Context, complaintId: String, listener: ComplaintListCallBack
    ) {
    }

    suspend fun getCurrentLocation(context: Context, listener: ComplaintListCallBack) {}
}