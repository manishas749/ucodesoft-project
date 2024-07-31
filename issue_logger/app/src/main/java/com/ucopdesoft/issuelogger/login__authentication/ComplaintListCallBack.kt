package com.ucopdesoft.issuelogger.login__authentication

import android.location.Address
import com.ucopdesoft.issuelogger.models.Comment
import com.ucopdesoft.issuelogger.models.Complaint

interface ComplaintListCallBack {

    fun onResponse(list: MutableList<Complaint>) {}

    fun onComplaintDetailResponse(complaint: Complaint?) {}

    fun onResponseLocation(address: MutableList<Address>) {}

    fun onComplaintImagesResponse(imagesList: List<String>?) {}

    fun onComplaintCommentsResponse(commentsList: List<Comment>?) {}

}