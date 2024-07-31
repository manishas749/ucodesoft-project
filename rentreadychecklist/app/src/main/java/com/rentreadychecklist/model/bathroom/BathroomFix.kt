package com.rentreadychecklist.model.bathroom

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class BathroomFix(
    var fix: String,
    var time: String,
    var product: String,
    var notes: String,
    var image: List<ImageUploadCommon>
)