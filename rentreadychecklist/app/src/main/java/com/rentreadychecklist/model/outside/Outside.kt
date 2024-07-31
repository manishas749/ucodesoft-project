package com.rentreadychecklist.model.outside

data class Outside(
    val items: OutsideItemList,
    var ok: OutsideOk,
    var na: String,
    var fix: OutSideFix
)