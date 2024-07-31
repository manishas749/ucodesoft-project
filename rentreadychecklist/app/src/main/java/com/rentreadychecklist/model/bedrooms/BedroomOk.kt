package com.rentreadychecklist.model.bedrooms

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class BedroomOk(
    var ok: String,
    var image: List<ImageUploadCommon>
)