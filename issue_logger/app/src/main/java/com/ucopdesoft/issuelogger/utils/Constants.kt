package com.ucopdesoft.issuelogger.utils

import com.ucopdesoft.issuelogger.models.Complaint
import com.ucopdesoft.issuelogger.models.User

class Constants {
    companion object {
        const val TOKEN = "token"
        var USERNAME = "user"
        const val PREFERENCES = "com.rentReady.preferences"
        const val COMPLAINT_ID_FORMAT = "yyyyMMddHHmmssSSS"

        const val USER_NAME = "userName"
        const val USER_NUMBER = "userNumber"
        const val USER_PROFILE_PIC = "profilePic"

        const val COMPLAINT_ID = "complaintId"
        const val UPVOTE_COUNT = "count_up"
        const val  DOWNVOTE_COUNT = "down"

        const val USER_ID = "userId"
        const val COMPLAINT_TITLE = "complaintTitle"
        const val COMPLAINT_DESCRIPTION = "complaintDescription"
        const val ADDRESS = "address"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val UP_VOTE = "upVote"
        const val DOWN_VOTE = "downVote"
        const val COMPLAINT_STATUS = "status"
        const val DATE = "date"
        const val COMMENT = "comment"

        const val VIEW_PAGER_VISIBILITY = "visibility"

        var complaintsList: List<Complaint> = listOf()
        var usersList: List<Pair<String, User>> = listOf()
    }
}