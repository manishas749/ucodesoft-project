package com.rentreadychecklist.db

import androidx.lifecycle.LiveData
import androidx.room.*
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

@Dao
interface AppDao {

    // Queries to get and insert,delete and update forms in database

    @Query("SELECT * FROM formInformation ORDER BY  rowId DESC")
    fun getAllFormInfo(): LiveData<List<AppData>>

    @Query("SELECT * FROM formInformation where id = :formId ")
    fun getFormInfoID(formId: Long): LiveData<List<AppData>>


    @Insert
    suspend fun insertForm(Form: AppData?): Long

    @Delete
    suspend fun deleteForm(Form: AppData?)

    @Update
    suspend fun updateForm(Form: AppData?)

    @Query("UPDATE formInformation set outside = :List WHERE id = :formId")
    suspend fun updateOutside(List: List<Outside>, formId: Long)

    @Query("UPDATE formInformation set homeScreen = :List WHERE id = :formId")
    suspend fun updateHomeScreen(List: List<HomeScreen>, formId: Long)

    @Query("UPDATE formInformation set frontDoors = :List WHERE id = :formId")
    suspend fun updateFrontDoors(List: List<FrontDoors>, formId: Long)

    @Query("UPDATE formInformation set garage = :List WHERE id = :formId")
    suspend fun updateGarage(List: List<Garage>, formId: Long)

    @Query("UPDATE formInformation set laundryRoom = :List WHERE id = :formId")
    suspend fun updateLaundryRoom(List: List<LaundryRoom>, formId: Long)

    @Query("UPDATE formInformation set livingRoom = :List WHERE id = :formId")
    suspend fun updateLivingRoom(List: List<LivingRoom>, formId: Long)

    @Query("UPDATE formInformation set greatRoom = :List WHERE id = :formId")
    suspend fun updateGreatRoom(List: List<GreatRoom>, formId: Long)

    @Query("UPDATE formInformation set diningRoom = :List WHERE id = :formId")
    suspend fun updateDiningRoom(List: List<DiningRoom>, formId: Long)

    @Query("UPDATE formInformation set kitchen = :List WHERE id = :formId")
    suspend fun updateKitchen(List: List<Kitchen>, formId: Long)

    @Query("UPDATE formInformation set miscellaneous = :List WHERE id = :formId")
    suspend fun updateMiscellaneous(List: List<Miscellaneous>, formId: Long)

    @Query("UPDATE formInformation set bedroom = :List WHERE id = :formId")
    suspend fun updateBedroom(List: List<Bedroom>, formId: Long)

    @Query("UPDATE formInformation set bathroom = :List WHERE id = :formId")
    suspend fun updateBathroom(List: List<Bathroom>, formId: Long)


}