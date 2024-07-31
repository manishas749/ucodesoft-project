package com.rentreadychecklist.model.laundry


data class LaundryRoom(
    val items: String,
    var ok: LaundryRoomOk,
    var na: String,
    var fix: LaundryRoomFix
)