package com.rentreadychecklist.model.greatroom

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class GreatRoomFix(
    var fix: String,
    var time: String,
    var product: String,
    var notes: String,
    var image: List<ImageUploadCommon>
)