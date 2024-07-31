package com.rentreadychecklist.model.miscellaneous

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class MiscellaneousFix(
    var fix: String,
    var time: String,
    var product: String,
    var notes: String,
    var image: List<ImageUploadCommon>
)