package com.rentreadychecklist.constants

import com.rentreadychecklist.model.bathroom.BathroomFix
import com.rentreadychecklist.model.bathroom.BathroomOk
import com.rentreadychecklist.model.bedrooms.BedroomFix
import com.rentreadychecklist.model.bedrooms.BedroomOk
import com.rentreadychecklist.model.diningRoom.DiningRoomFix
import com.rentreadychecklist.model.diningRoom.DiningRoomOk
import com.rentreadychecklist.model.frontdoors.FrontDoorFix
import com.rentreadychecklist.model.frontdoors.FrontDoorOk
import com.rentreadychecklist.model.garage.GarageDoorsOk
import com.rentreadychecklist.model.garage.GarageFix
import com.rentreadychecklist.model.greatroom.GreatRoomFix
import com.rentreadychecklist.model.greatroom.GreatRoomOk
import com.rentreadychecklist.model.kitchen.KitchenFix
import com.rentreadychecklist.model.kitchen.KitchenOk
import com.rentreadychecklist.model.laundry.LaundryRoomFix
import com.rentreadychecklist.model.laundry.LaundryRoomOk
import com.rentreadychecklist.model.livingRoom.LivingRoomFix
import com.rentreadychecklist.model.livingRoom.LivingRoomOk
import com.rentreadychecklist.model.miscellaneous.MiscellaneousFix
import com.rentreadychecklist.model.miscellaneous.MiscellaneousOk
import com.rentreadychecklist.model.outside.OutSideFix
import com.rentreadychecklist.model.outside.OutsideOk
/**
 * This class contains constant variables like RentReady google drive link and images list.
 */
class Constants {
    companion object {
        const val PREFERENCES = "com.rentReady.preferences"
        const val DOCUMENT_LINK =
            "https://drive.google.com/drive/mobile/folders/172-MVEMmXFq7Ore_Tpu1Hyf1L1HXO2YG?usp=share_link"
        const val INCIDENT_REPORT_LINK = "https://forms.gle/6xw3iodNyNwkFKy38"
        const val VEHICLE_INSURANCE_LINK =
            "https://drive.google.com/drive/folders/1Q6WNca-Y-wWYjSUUxdvgLdD1uZepp2Lz?usp=sharing"
        var FORMID: Long = 0
        var newAndSavedChecklist = "new"
        var getItemPosition = 0
        var formName = ""
        var bedroomPosition = 0
        var bathroomPosition = 0
        var fixPosition = 0
        var time = ""
        var product = ""
        var notes = ""
        val fixList = mutableListOf<OutSideFix>()
        val garageFixList = mutableListOf<GarageFix>()
        val bedroomFixList = mutableListOf<List<BedroomFix>>()
        var bedroomFixPosition = 0
        val bathroomFixList = mutableListOf<List<BathroomFix>>()
        var bathroomFixPosition = 0

        val frontDoorFixList = mutableListOf<FrontDoorFix>()
        val laundryFixList = mutableListOf<LaundryRoomFix>()
        val livingRoomFixList = mutableListOf<LivingRoomFix>()
        val greatRoomFixList = mutableListOf<GreatRoomFix>()
        val diningRoomFixList = mutableListOf<DiningRoomFix>()
        val kitchenFixList = mutableListOf<KitchenFix>()
        val miscellaneousFixList = mutableListOf<MiscellaneousFix>()

        var imageTitle = ""
        const val EMAIL = "email"
        var pdfDownloadOrPrivate = ""

        // uploading images for ok
        var okImagePosition = 0
        var imageFormName = ""
        var okImagesTitle = ""
        var okImageBedroomPosition = 0
        var okImageBathroomPosition = 0
        val outsideOkImageList = mutableListOf<OutsideOk>()
        val frontDoorOkImageList = mutableListOf<FrontDoorOk>()
        val garageOkImageList = mutableListOf<GarageDoorsOk>()
        val laundryOkImageList = mutableListOf<LaundryRoomOk>()
        val livingOkImageList = mutableListOf<LivingRoomOk>()
        val greatOkImageList = mutableListOf<GreatRoomOk>()
        val diningOkImageList = mutableListOf<DiningRoomOk>()
        val kitchenOkImageList = mutableListOf<KitchenOk>()
        val miscellaneousOkImageList = mutableListOf<MiscellaneousOk>()
        val bedroomOkImageList = mutableListOf<List<BedroomOk>>()
        val bathroomOkImageList = mutableListOf<List<BathroomOk>>()
    }
}