package com.rentreadychecklist.model.diningRoom

import com.rentreadychecklist.model.imageupload.ImageUploadCommon

data class DiningRoomFix (
    var fix :String,
    var time:String,
    var product:String,
    var notes :String,
    var image:List<ImageUploadCommon>
)