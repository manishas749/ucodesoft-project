package com.rentreadychecklist.model.bedrooms

data class Bedrooms(
    val items: BedroomItemList,
    var ok: BedroomOk,
    var na: String,
    var fix: BedroomFix
)