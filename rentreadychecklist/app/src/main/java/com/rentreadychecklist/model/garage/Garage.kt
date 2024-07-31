package com.rentreadychecklist.model.garage


data class Garage(
    val items:GarageItemList,
    var ok:GarageDoorsOk,
    var na:String,
    var fix: GarageFix
)