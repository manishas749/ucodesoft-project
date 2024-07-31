package com.rentreadychecklist.model.kitchen

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class KitchenOk(
    var ok: String,
    var image: List<ImageUploadCommon>
)