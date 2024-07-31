package com.rentreadychecklist.model.bathroom


data class Bathrooms(
    val items: BathroomItemList,
    var ok: BathroomOk,
    var na: String,
    var fix: BathroomFix
)