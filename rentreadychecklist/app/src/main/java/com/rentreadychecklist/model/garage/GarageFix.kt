package com.rentreadychecklist.model.garage

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class GarageFix(
    var fix: String,
    var time: String,
    var product: String,
    var notes: String,
    var image: List<ImageUploadCommon>
)