package com.rentreadychecklist.model.miscellaneous


data class Miscellaneous(
    val items: MiscellaneousItemList,
    var ok: MiscellaneousOk,
    var na: String,
    var fix: MiscellaneousFix
)