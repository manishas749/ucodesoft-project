package com.rentreadychecklist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.db.AppData
import com.rentreadychecklist.db.RoomAppDb
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
import com.rentreadychecklist.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * AppViewModel use for save and get data from RoomDatabase using repository.
 */
class AppViewModel(app: Application) : AndroidViewModel(app) {
    val readFormData: LiveData<List<AppData>>     // to read all the list from Database
    var readFormDataID: LiveData<List<AppData>>   // to read all the list from Database where formId passed
    private val repository: AppRepository

    init {
        val dao = RoomAppDb.getAppDatabase(getApplication()).userDao()
        repository = dao?.let { AppRepository(it) }!!
        readFormData = repository.readFormData
        readFormDataID = repository.readFormDataID
    }


    // To insert form in database
    fun insertFormInfo(formData: AppData) {

        viewModelScope.launch()
        {
            FORMID = repository.addFormData(formData)
        }
    }

    // To delete form in database
    fun deleteFormInfo(formData: AppData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFormData(formData)
        }
    }

    // To update outside form in database
    fun updateOutside(List: List<Outside>, FormId: Long) {
        viewModelScope.launch {
            repository.updateOutside(List, FormId)
        }
    }

    // To update homeScreen form in database
    fun updateHomeScreen(List: List<HomeScreen>, FormId: Long) {
        viewModelScope.launch {
            repository.updateHomeScreen(List, FormId)
        }
    }

    // To update FrontDoors form in database
    fun updateFrontDoors(List: List<FrontDoors>, FormId: Long) {
        viewModelScope.launch {
            repository.updateFrontDoors(List, FormId)
        }
    }

    // To update garage form in database
    fun updateGarage(List: List<Garage>, FormId: Long) {
        viewModelScope.launch {
            repository.updateGarage(List, FormId)
        }
    }

    // To update laundry form in database
    fun updateLaundryRoom(List: List<LaundryRoom>, FormId: Long) {
        viewModelScope.launch {
            repository.updateLaundryRoom(List, FormId)
        }
    }

    // To update livingRoom form in database
    fun updateLivingRoom(List: List<LivingRoom>, FormId: Long) {
        viewModelScope.launch {
            repository.updateLivingRoom(List, FormId)
        }
    }

    // To update GreatRoom form in database
    fun updateGreatRoom(List: List<GreatRoom>, FormId: Long) {
        viewModelScope.launch {
            repository.updateGreatRoom(List, FormId)
        }
    }

    // To update DiningRoom form in Database
    fun updateDiningRoom(List: List<DiningRoom>, FormId: Long) {
        viewModelScope.launch {
            repository.updateDiningRoom(List, FormId)
        }
    }

    // To update kitchen form in database
    fun updateKitchen(List: List<Kitchen>, FormId: Long) {
        viewModelScope.launch {
            repository.updateKitchen(List, FormId)
        }
    }

    // To update miscellaneous form in database
    fun updateMiscellaneous(List: List<Miscellaneous>, FormId: Long) {
        viewModelScope.launch {
            repository.updateMiscellaneous(List, FormId)
        }
    }

    // To update bedroom form in database
    fun updateBedroom(List: List<Bedroom>, FormId: Long) {
        viewModelScope.launch {
            repository.updateBedroom(List, FormId)
        }
    }

    // To update bathroom form in database
    fun updateBathroom(List: List<Bathroom>, FormId: Long) {
        viewModelScope.launch {

            repository.updateBathroom(List, FormId)
        }
    }

}