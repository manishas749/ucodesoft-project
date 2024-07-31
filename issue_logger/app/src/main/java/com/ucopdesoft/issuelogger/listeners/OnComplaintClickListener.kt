package com.ucopdesoft.issuelogger.listeners

import com.ucopdesoft.issuelogger.models.Complaint

interface OnComplaintClickListener {
    fun onClick(complaint: Complaint, upvoteCount: String, downVoteCount: String) {}
}