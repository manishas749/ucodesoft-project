package com.rentreadychecklist.model.garage

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class GarageDoorsOk(
    var ok: String,
    var image: List<ImageUploadCommon>
)