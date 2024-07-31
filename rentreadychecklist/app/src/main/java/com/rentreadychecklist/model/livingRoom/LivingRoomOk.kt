package com.rentreadychecklist.model.livingRoom

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class LivingRoomOk(
    var ok: String,
    var image: List<ImageUploadCommon>
)