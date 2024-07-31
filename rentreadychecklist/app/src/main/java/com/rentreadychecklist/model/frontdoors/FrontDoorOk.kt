package com.rentreadychecklist.model.frontdoors

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class FrontDoorOk(
    var ok: String,
    var image: List<ImageUploadCommon>
)