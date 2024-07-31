package com.rentreadychecklist.model.laundry

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class LaundryRoomOk(
    var ok: String,
    var image: List<ImageUploadCommon>
)