package com.rentreadychecklist.model.greatroom

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class GreatRoomOk(
    var ok: String,
    var image: List<ImageUploadCommon>
)