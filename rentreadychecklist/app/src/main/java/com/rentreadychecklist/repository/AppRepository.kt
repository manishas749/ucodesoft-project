package com.rentreadychecklist.repository

import androidx.lifecycle.LiveData
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.db.AppDao
import com.rentreadychecklist.db.AppData
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

/**
 * AppRepository used for save and get data form RoomDatabase (App DAO).
 */
class AppRepository(private val dao: AppDao) {

    val readFormData: LiveData<List<AppData>> =
        dao.getAllFormInfo()       // to read all the list from Database
    val readFormDataID: LiveData<List<AppData>> =
        dao.getFormInfoID(formId = FORMID)     // to read all the list from Database where formId passed


    // To insert form in database
    suspend fun addFormData(formData: AppData): Long {
        return dao.insertForm(formData)
    }

    // To delete form in database
    suspend fun deleteFormData(formData: AppData) {
        dao.deleteForm(formData)
    }

    // To update outside form in database
    suspend fun updateOutside(List: List<Outside>, formId: Long) {
        dao.updateOutside(List, formId)
    }

    // To update homeScreen form in database
    suspend fun updateHomeScreen(List: List<HomeScreen>, formId: Long) {
        dao.updateHomeScreen(List, formId)
    }

    // To update FrontDoors form in database
    suspend fun updateFrontDoors(List: List<FrontDoors>, formId: Long) {
        dao.updateFrontDoors(List, formId)
    }

    // To update garage form in database
    suspend fun updateGarage(List: List<Garage>, formId: Long) {
        dao.updateGarage(List, formId)
    }

    // To update laundry form in database
    suspend fun updateLaundryRoom(List: List<LaundryRoom>, formId: Long) {
        dao.updateLaundryRoom(List, formId)
    }

    // To update livingRoom form in database
    suspend fun updateLivingRoom(List: List<LivingRoom>, formId: Long) {
        dao.updateLivingRoom(List, formId)
    }

    // To update greatRoom form in database
    suspend fun updateGreatRoom(List: List<GreatRoom>, formId: Long) {
        dao.updateGreatRoom(List, formId)
    }


    // To update DiningRoom form in Database
    suspend fun updateDiningRoom(List: List<DiningRoom>, formId: Long) {
        dao.updateDiningRoom(List, formId)
    }

    // To update kitchen form in database
    suspend fun updateKitchen(List: List<Kitchen>, formId: Long) {
        dao.updateKitchen(List, formId)
    }

    // To update miscellaneous form in database
    suspend fun updateMiscellaneous(List: List<Miscellaneous>, formId: Long) {
        dao.updateMiscellaneous(List, formId)
    }

    // To update bedroom form in database
    suspend fun updateBedroom(List: List<Bedroom>, formId: Long) {
        dao.updateBedroom(List, formId)
    }

    // To update bathroom form in database
    suspend fun updateBathroom(List: List<Bathroom>, formId: Long) {
        dao.updateBathroom(List, formId)
    }

}