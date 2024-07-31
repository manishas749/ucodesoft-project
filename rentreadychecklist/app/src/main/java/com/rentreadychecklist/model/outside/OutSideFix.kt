package com.rentreadychecklist.model.outside

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class OutSideFix(
    var fix: String,
    var time: String,
    var product: String,
    var notes: String,
    var image: List<ImageUploadCommon>
)