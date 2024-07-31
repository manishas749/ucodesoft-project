package com.rentreadychecklist.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rentreadychecklist.model.bathroom.Bathroom
import com.rentreadychecklist.model.bedrooms.Bedroom
import com.rentreadychecklist.model.diningRoom.DiningRoom
import com.rentreadychecklist.model.frontdoors.FrontDoors
import com.rentreadychecklist.model.garage.Garage
import com.rentreadychecklist.model.greatroom.GreatRoom
import com.rentreadychecklist.model.homescreen.HomeScreen
import com.rentreadychecklist.model.kitchen.Kitchen
import com.rentreadychecklist.model.laundry.LaundryRoom
import com.rentreadychecklist.model.livingRoom.LivingRoom
import com.rentreadychecklist.model.miscellaneous.Miscellaneous
import com.rentreadychecklist.model.outside.Outside

@Entity(tableName = "formInformation")
data class  AppData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "HomeScreen") val homeScreen: List<HomeScreen>,
    @ColumnInfo(name = "Outside") val outside: List<Outside>,
    @ColumnInfo(name = "FrontDoors") val frontDoors: List<FrontDoors>,
    @ColumnInfo(name = "Garage") val garage: List<Garage>,
    @ColumnInfo(name = "LaundryRoom") val laundryRoom: List<LaundryRoom>,
    @ColumnInfo(name = "LivingRoom") val livingRoom: List<LivingRoom>,
    @ColumnInfo(name = "GreatRoom") val greatRoom: List<GreatRoom>,
    @ColumnInfo(name = "DiningRoom") val diningRoom: List<DiningRoom>,
    @ColumnInfo(name = "Kitchen") val kitchen: List<Kitchen>,
    @ColumnInfo(name = "Miscellaneous") val miscellaneous: List<Miscellaneous>,
    @ColumnInfo(name = "Bedroom") val bedroom: List<Bedroom>,
    @ColumnInfo(name = "Bathroom") val bathroom: List<Bathroom>
)