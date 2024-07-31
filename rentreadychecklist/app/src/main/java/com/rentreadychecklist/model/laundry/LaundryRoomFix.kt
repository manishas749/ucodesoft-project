package com.rentreadychecklist.model.laundry

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class LaundryRoomFix(
    var fix: String,
    var time: String,
    var product: String,
    var notes: String,
    var image: List<ImageUploadCommon>
)