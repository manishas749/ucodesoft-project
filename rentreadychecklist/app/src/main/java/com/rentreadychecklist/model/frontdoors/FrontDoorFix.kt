package com.rentreadychecklist.model.frontdoors

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class FrontDoorFix(
    var fix: String,
    var time: String,
    var product: String,
    var notes: String,
    var image: List<ImageUploadCommon>
)