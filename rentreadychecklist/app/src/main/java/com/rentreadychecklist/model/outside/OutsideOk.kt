package com.rentreadychecklist.model.outside

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class OutsideOk(
    var ok: String,
    var image: List<ImageUploadCommon>
)