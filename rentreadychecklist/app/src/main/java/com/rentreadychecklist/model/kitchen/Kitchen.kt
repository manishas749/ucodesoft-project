package com.rentreadychecklist.model.kitchen


data class Kitchen(
    val items: KitchenItemList,
    var ok: KitchenOk,
    var na: String,
    var fix: KitchenFix
)