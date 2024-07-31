package com.rentreadychecklist.model.homescreen

import com.rentreadychecklist.model.imageupload.ImageUploadCommon


data class HomeScreen(
    var date: String,
    var time: String,
    var propertyAddress: String,
    var addressCondition: Boolean,
    var frontDoorCondition: Boolean,
    var lockSetCondition: Boolean,
    var image: List<ImageUploadCommon>,
    var additionalNotes: String
)