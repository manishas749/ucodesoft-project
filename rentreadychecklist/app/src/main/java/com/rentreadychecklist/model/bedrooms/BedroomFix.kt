package com.rentreadychecklist.model.bedrooms

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class BedroomFix(
    var fix: String,
    var time: String,
    var product: String,
    var notes: String,
    var image: List<ImageUploadCommon>
)