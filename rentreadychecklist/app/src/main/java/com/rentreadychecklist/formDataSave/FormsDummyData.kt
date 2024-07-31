package com.rentreadychecklist.formDataSave

import com.rentreadychecklist.model.bathroom.*
import com.rentreadychecklist.model.bedrooms.*
import com.rentreadychecklist.model.diningRoom.DiningRoom
import com.rentreadychecklist.model.diningRoom.DiningRoomFix
import com.rentreadychecklist.model.diningRoom.DiningRoomItemList
import com.rentreadychecklist.model.diningRoom.DiningRoomOk
import com.rentreadychecklist.model.frontdoors.FrontDoorFix
import com.rentreadychecklist.model.frontdoors.FrontDoorOk
import com.rentreadychecklist.model.frontdoors.FrontDoors
import com.rentreadychecklist.model.garage.Garage
import com.rentreadychecklist.model.garage.GarageDoorsOk
import com.rentreadychecklist.model.garage.GarageFix
import com.rentreadychecklist.model.garage.GarageItemList
import com.rentreadychecklist.model.greatroom.GreatRoom
import com.rentreadychecklist.model.greatroom.GreatRoomFix
import com.rentreadychecklist.model.greatroom.GreatRoomItemList
import com.rentreadychecklist.model.greatroom.GreatRoomOk
import com.rentreadychecklist.model.homescreen.HomeScreen
import com.rentreadychecklist.model.imageupload.ImageUploadCommon
import com.rentreadychecklist.model.kitchen.Kitchen
import com.rentreadychecklist.model.kitchen.KitchenFix
import com.rentreadychecklist.model.kitchen.KitchenItemList
import com.rentreadychecklist.model.kitchen.KitchenOk
import com.rentreadychecklist.model.laundry.LaundryRoom
import com.rentreadychecklist.model.laundry.LaundryRoomFix
import com.rentreadychecklist.model.laundry.LaundryRoomOk
import com.rentreadychecklist.model.livingRoom.LivingRoom
import com.rentreadychecklist.model.livingRoom.LivingRoomFix
import com.rentreadychecklist.model.livingRoom.LivingRoomItemList
import com.rentreadychecklist.model.livingRoom.LivingRoomOk
import com.rentreadychecklist.model.miscellaneous.Miscellaneous
import com.rentreadychecklist.model.miscellaneous.MiscellaneousFix
import com.rentreadychecklist.model.miscellaneous.MiscellaneousItemList
import com.rentreadychecklist.model.miscellaneous.MiscellaneousOk
import com.rentreadychecklist.model.outside.OutSideFix
import com.rentreadychecklist.model.outside.Outside
import com.rentreadychecklist.model.outside.OutsideItemList
import com.rentreadychecklist.model.outside.OutsideOk

/**
 * This class used for get Dummy Form Data.
 */
class FormsDummyData {
    companion object {
        var outsideData: MutableList<Outside> = mutableListOf()
        var frontDoorsData: MutableList<FrontDoors> = mutableListOf()
        var garageData: MutableList<Garage> = mutableListOf()
        var laundryRoomData: MutableList<LaundryRoom> = mutableListOf()
        var livingRoomData: MutableList<LivingRoom> = mutableListOf()
        var greatRoomData: MutableList<GreatRoom> = mutableListOf()
        var diningRoomData: MutableList<DiningRoom> = mutableListOf()
        var kitchenData: MutableList<Kitchen> = mutableListOf()
        var miscellaneousData: MutableList<Miscellaneous> = mutableListOf()
        var numberOfBedroom: MutableList<Bedroom> = mutableListOf()
        var numberOfBathroom: MutableList<Bathroom> = mutableListOf()
        var homeScreenData: MutableList<HomeScreen> = mutableListOf()
    }

    var bedroom: MutableList<Bedrooms> = mutableListOf()
    var bathroom: MutableList<Bathrooms> = mutableListOf()

    fun clearConstantData()
    {
        homeScreenData.clear()
        outsideData.clear()
        frontDoorsData.clear()
        garageData.clear()
        laundryRoomData.clear()
        livingRoomData.clear()
        greatRoomData.clear()
        diningRoomData.clear()
        kitchenData.clear()
        miscellaneousData.clear()
        numberOfBedroom.clear()
        numberOfBathroom.clear()
    }

    fun homeScreenDataAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        homeScreenData.add(
            HomeScreen(
                "", "", "",
                addressCondition = false,
                frontDoorCondition = false,
                lockSetCondition = false,
                image = imageList,
                additionalNotes = ""
            )
        )

    }

    fun outsideDataAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()
        outsideData.add(
            Outside(
                OutsideItemList(
                    "Fence:",
                    ItemCondition1 = false,
                    ItemCondition2 = false
                ),
                OutsideOk("", okImageList), "", OutSideFix(
                    "", "", "", "",
                    imageList
                )
            )
        )
        outsideData.add(
            Outside(
                OutsideItemList(
                    "Gate:",
                    ItemCondition1 = false,
                    ItemCondition2 = false
                ), OutsideOk("", okImageList), "", OutSideFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        outsideData.add(
            Outside(
                OutsideItemList(
                    "Porch/Coach Light:",
                    ItemCondition1 = false,
                    ItemCondition2 = false
                ), OutsideOk("", okImageList), "", OutSideFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        outsideData.add(
            Outside(
                OutsideItemList(
                    "Anti-Siphon Breaker Value And Insulation:",
                    ItemCondition1 = false,
                    ItemCondition2 = false
                ), OutsideOk("", okImageList), "", OutSideFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        outsideData.add(
            Outside(
                OutsideItemList(
                    "Power Washing Driveway:",
                    ItemCondition1 = false,
                    ItemCondition2 = false
                ), OutsideOk("", okImageList), "", OutSideFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        outsideData.add(
            Outside(
                OutsideItemList(
                    "Weatherproof Receptacle Cover At Front Door:",
                    ItemCondition1 = false,
                    ItemCondition2 = false
                ), OutsideOk("", okImageList), "", OutSideFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        outsideData.add(
            Outside(
                OutsideItemList(
                    "Stucco:",
                    ItemCondition1 = false,
                    ItemCondition2 = false
                ), OutsideOk("", okImageList), "", OutSideFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        outsideData.add(
            Outside(
                OutsideItemList(
                    "Shutters:",
                    ItemCondition1 = false,
                    ItemCondition2 = false
                ), OutsideOk("", okImageList), "", OutSideFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        outsideData.add(
            Outside(
                OutsideItemList(
                    "Satellite Dish And Cables:",
                    ItemCondition1 = false,
                    ItemCondition2 = false
                ), OutsideOk("", okImageList), "", OutSideFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        outsideData.add(
            Outside(
                OutsideItemList(
                    "Address Numbers Or Letters:",
                    ItemCondition1 = false,
                    ItemCondition2 = false
                ), OutsideOk("", okImageList), "", OutSideFix(
                    "", "", "",
                    "", imageList
                )
            )
        )

    }

    fun frontDoorsDataAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        frontDoorsData.add(
            FrontDoors(
                "Doors:",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )
        frontDoorsData.add(
            FrontDoors(
                "Astragal:",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )
        frontDoorsData.add(
            FrontDoors(
                "Door Bell:",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )
        frontDoorsData.add(
            FrontDoors(
                "Door Locks:",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )
        frontDoorsData.add(
            FrontDoors(
                "Door Knobs:",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )
        frontDoorsData.add(
            FrontDoors(
                "Sagging Hinges:",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )
        frontDoorsData.add(
            FrontDoors(
                "Strike Plates:",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )
        frontDoorsData.add(
            FrontDoors(
                "Missing Hardware On Doors:",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )
        frontDoorsData.add(
            FrontDoors(
                "Peep Hole (Height 60” From The Bottom Of Door):",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )
        frontDoorsData.add(
            FrontDoors(
                "Door Stops:",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )
        frontDoorsData.add(
            FrontDoors(
                "Weather Stripping:",
                FrontDoorOk("", imageList),
                "",
                FrontDoorFix("", "", "", "", imageList)
            )
        )

    }

    fun garageDataAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        garageData.add(
            Garage(
                GarageItemList("Garage Door Works:", ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList), "", GarageFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Garage Door Opener:", ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Garage Door To House Self-Closing Hinges:", ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Locks/ Knobs:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Cabinets:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Electrical",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Lights:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Light Bulbs Needed:", ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Ceiling Fans:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Outlets:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Covers:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Service Covers:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Lighting:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Power Wash Floor:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Water Softener Removal:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Water Heater",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Hot Water Heater Earthquake Straps:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Hot Water Heater Collection Pan With Hose Leading To Outside Overflow:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Drain To Outside:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("TPRV Flows Down:",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )

        garageData.add(
            Garage(
                GarageItemList("Walls/ Baseboards/ Ceiling",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Paint :",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Drywall Damage :",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Remove / Repair Nails, Hangers, Etc :",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )
        garageData.add(
            Garage(
                GarageItemList("Evidence Of Water Damage :",ItemCondition1 = false, ItemCondition2 = false),
                GarageDoorsOk("", imageList),
                "",
                GarageFix("", "", "", "", imageList)
            )
        )



    }

    fun laundryRoomAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        laundryRoomData.add(
            LaundryRoom(
                "Clothes Washer:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Washer Shut Off Valves:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Hose And Supply Line:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Dryer:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Lint Screen:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Lint Path:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Walls/ Damage:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Doors:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Door Locks/Knobs:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Door Stops:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Missing Hardware On Doors:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Lights:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Light Bulbs Needed:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Vent Fans:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Outlets:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Covers:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Service Covers:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Cabinet Doors and Handles:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Cabinet Drawers:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )
        laundryRoomData.add(
            LaundryRoom(
                "Shelves:",
                LaundryRoomOk("", imageList),
                "",
                LaundryRoomFix("", "", "", "", imageList)
            )
        )

    }

    fun livingRoomAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Vent Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ),
                LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Carpet And Flooring",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Carpet :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Loose Or Cracked Tiles Or Damaged Floor :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Walls/ Baseboards/ Ceiling",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Paint :", ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Drywall Damage :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Remove / Repair Nails, Hangers, Etc :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Evidence Of Water Damage :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Windows",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Open And Close Easily :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Screens (Each Window And Sliding Glass Door) :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Locking Device :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Broken, Cracked Or Missing :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Blinds :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Condensation Between Window Panes :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Electrical",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Lights :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Light Bulbs Needed :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Ceiling Fans :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Outlets :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Service Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Closets",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Closet Door Guide/Track, Handles :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Closet Door Open And Close :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Racks, Rods, Shelves :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Fireplace",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        livingRoomData.add(
            LivingRoom(
                LivingRoomItemList(
                    "Missing Key :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), LivingRoomOk("", imageList), "", LivingRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )


    }

    fun greatRoomAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Vent Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ),
                GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Carpet And Flooring",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Carpet :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Loose Or Cracked Tiles Or Damaged Floor :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Walls/ Baseboards/ Ceiling",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Paint :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Drywall Damage :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Remove / Repair Nails, Hangers, Etc :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Evidence Of Water Damage :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Windows/ Exterior Doors",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Open And Close Easily :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Screens (Each Window And Sliding Glass Door) :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Locking Device :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Broken, Cracked Or Missing :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Blinds :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Exterior Door Security Locking Device :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Condensation Between Window Panes :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Doors",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Doors :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Astragal :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Door Locks/ Knobs :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Missing Hardware On Doors :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Door Stops :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Electrical",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Lights :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Light Bulbs Needed :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Ceiling Fans:",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Outlets :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Service Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Closets",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Closet Door Guide/Track, Handles :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Closet Door Open And Close :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Racks, Rods, Shelves :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Fireplace",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )
        greatRoomData.add(
            GreatRoom(
                GreatRoomItemList(
                    "Missing Key :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), GreatRoomOk("", imageList), "",
                GreatRoomFix("", "", "", "", imageList)
            )
        )


    }

    fun diningRoomAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Vent Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ),
                DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Carpet And Flooring", false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Carpet :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Loose Or Cracked Tiles Or Damaged Floor :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Walls/ Baseboards/ Ceiling",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Paint :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Drywall Damage :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Remove / Repair Nails, Hangers, Etc :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Evidence Of Water Damage :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Windows/ Exterior Doors",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Open And Close Easily :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Screens (Each Window And Sliding Glass Door) :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Locking Device :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Broken, Cracked Or Missing :", false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Blinds :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Exterior Door Security Locking Device :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Condensation Between Window Panes :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Doors",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Doors :", false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Astragal :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Door Locks/ Knobs :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Missing Hardware On Doors :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Door Stops :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Electrical",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Lights :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Light Bulbs Needed :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Ceiling Fans:",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Outlets :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Service Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Fireplace", false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        diningRoomData.add(
            DiningRoom(
                DiningRoomItemList(
                    "Missing Key :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), DiningRoomOk("", imageList), "", DiningRoomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )


    }

    fun kitchenAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Grout And Caulk In Kitchen :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ),
                KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Cabinet Doors And Handles :", false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Shelves :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Cabinet Drawers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Filter In Vent Hood :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Hood Light And Fan :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Vent Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Island :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Electrical",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Lights :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Light Bulbs Needed :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Ceiling Fans :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Outlets :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Service Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Sink",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Faucets :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Garbage Disposal :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Air Gap Cap :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Guard :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Under Sinks Leak :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Damage Under Sink :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Appliances(Check For Filters, Knobs, Racks, Shelves, Drip Pans, Light Bulbs) \n Take Condition Photos And Modal Number For All Appliances",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Refrigerator :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Oven :", false, ItemCondition2 = false,
                    "", "", "", ""
                ),
                KitchenOk("", imageList),
                "",
                KitchenFix("", "", "", "", imageList)
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Dishwasher :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Microwave :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Pantry",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Pantry Door Guide/Track, Handles :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Pantry Door Open And Close :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Racks, Rods, Shelves :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Windows",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Open And Close Easily :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Screens (Each Window And Sliding Glass Door) :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Locking Device :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Broken, Cracked Or Missing :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Blinds :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        kitchenData.add(
            Kitchen(
                KitchenItemList(
                    "Condensation Between Window Panes :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    "", "", "", ""
                ), KitchenOk("", imageList), "", KitchenFix(
                    "", "", "",
                    "", imageList
                )
            )
        )

    }

    fun miscellaneousAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Cleaning",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ),
                MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Tennent Personal Belongings :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Furniture :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Hire Outside Company For Deep Cleaning :", false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Smoke Detectors (One In Each Bedroom, Hall, And Living Room)",
                    false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Batteries In Smoke Detectors: Count", false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Smoke Detectors Replace Because They Don’t Work Or Are Missing: Count :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "C/O In Each Hall :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "HAVC",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "A/C (Requires Professional Diagnoses) :", false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "A/C Vents: Replace :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "A/C Filters: Replace :", false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "A/C Filters Cover, Caps, Screws :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Foam Around Compressor :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Stairs/ Hallway",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Outlets :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Service Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Lights :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Light Bulbs Needed :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Floor Covering :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Hand Rails :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Walls :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )
        miscellaneousData.add(
            Miscellaneous(
                MiscellaneousItemList(
                    "Blinds :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    itemNotes4 = ""
                ), MiscellaneousOk("", imageList), "", MiscellaneousFix(
                    "", "",
                    "", "", imageList
                )
            )
        )

    }

    fun bedroomAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Vent Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ),
                BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Carpet And Flooring",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Carpet :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Loose Or Cracked Tiles Or Damaged Floor :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Walls/ Baseboards/ Ceiling",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Paint :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Drywall Damage :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Remove / Repair Nails, Hangers, Etc :", false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Evidence Of Water Damage :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Windows/ Sliding Glass Doors",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Open And Close Easily :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Screens (Each Window And Sliding Glass Doors) :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Locking Device :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Broken, Cracked Or Missing :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Condensation Between Panes :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Blinds :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Exterior Door Security Locking Device :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Doors",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Doors :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Astragal :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Door Locks/ Knobs :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Missing Hardware On Doors :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Door Stops :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Electrical",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Lights :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Light Bulbs Needed :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Ceiling Fans :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Outlets :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Service Covers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Closets", ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Closet Door Pins, Gliders, Handles :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Closet Door Open And Close :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bedroom.add(
            Bedrooms(
                BedroomItemList(
                    "Racks, Rods, Shelves :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = "",
                    itemNotes4 = ""
                ), BedroomOk("", imageList), "", BedroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )

        numberOfBedroom.add(Bedroom("Bedroom", bedroom))


    }

    fun bathroomAdd() {
        val imageList = arrayListOf<ImageUploadCommon>()
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Towel Bars :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ),
                BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Toilet Paper Holders :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "GFCI And Outlets :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Lights Switch And Fan Switch :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Exhaust Fan Vent :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Light Bulb Replace :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Shower/Tub",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Shower Head :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Shower Handle :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Shower/Tub Doors :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Water Drain :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Tub Stoppers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Grout And Caulk In Bathroom :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Shower Diverter :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Sink",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Shelves And Drawers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Medicine Cabinet :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Shelves :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Grout And Calking :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Faucet :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Aerators :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Water Drain :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Sink Stoppers :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Water Leak Or Damage Under Sink :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Toilet",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Toilet Leaks Or Obstructions :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Toilet Seats :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Caulk Around Base Of Toilet :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Flush Valve And Fill Valve :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Toilet Loose At Floor :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Doors",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Doors :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Astragal :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Door Locks/Knobs :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )
        bathroom.add(
            Bathrooms(
                BathroomItemList(
                    "Door Stops /Missing Hardware On Doors :",
                    ItemCondition1 = false,
                    ItemCondition2 = false,
                    ItemNotes1 = "",
                    ItemNotes2 = "",
                    ItemNotes3 = ""
                ), BathroomOk("", imageList), "", BathroomFix(
                    "", "", "",
                    "", imageList
                )
            )
        )

        numberOfBathroom.add(Bathroom("Bathroom", bathroom))

    }

}