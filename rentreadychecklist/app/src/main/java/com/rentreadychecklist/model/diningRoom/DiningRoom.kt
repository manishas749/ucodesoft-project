package com.rentreadychecklist.model.diningRoom

data class DiningRoom(
    val items: DiningRoomItemList,
    var ok: DiningRoomOk,
    var na: String,
    var fix: DiningRoomFix
)