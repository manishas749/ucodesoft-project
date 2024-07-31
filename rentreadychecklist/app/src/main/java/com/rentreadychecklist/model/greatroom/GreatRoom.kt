package com.rentreadychecklist.model.greatroom

data class GreatRoom(
    val items: GreatRoomItemList,
    var ok: GreatRoomOk,
    var na: String,
    var fix: GreatRoomFix
)