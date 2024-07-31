package com.rentreadychecklist.model.livingRoom

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class LivingRoomFix(
    var fix: String,
    var time: String,
    var product: String,
    var notes: String,
    var image: List<ImageUploadCommon>
)