package com.rentreadychecklist.model.frontdoors


data class FrontDoors(
    val items:String,
    var ok:FrontDoorOk,
    var NA:String,
    var fix: FrontDoorFix
)