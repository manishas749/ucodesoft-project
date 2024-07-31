package com.rentreadychecklist.model.kitchen

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class KitchenFix(
    var fix: String,
    var time: String,
    var product: String,
    var notes: String,
    var image: List<ImageUploadCommon>
)