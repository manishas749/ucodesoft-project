package com.rentreadychecklist.formDataSave

/**
 * This class used for get items for specific form screen.
 */
class FormItemList {

    companion object {

        val outsideItems = arrayListOf(
            "Fence :",
            "Gate :",
            "Porch/Coach Light :",
            "Anti- Siphon Breaker Valve And Insulation :",
            "Power Washing Driveway :",
            "Weatherproof Receptacle Cover At Front Door :",
            "Stucco :",
            "Shutters :",
            "Satellite Dish and Cables :",
            "Address Numbers Or Letters :"
        )


        val miscellaneousItems = arrayListOf(
            "Cleaning",
            "Tennent Personal Belongings :",
            "Furniture :",
            "Hire Outside Company For Deep Cleaning :",
            "Smoke Detector s (One In Each Bedroom, Hall, And Living Room)",
            "Batteries In Smoke Detectors: Count",
            "Smoke Detectors Replace Because They Don’t Work Or Are Missing: Count :",
            "C/O In Each Hall :",
            "A/C (Requires Professional Diagnoses) :",
            "A/C Vents: Replace :",
            "A/C Filters: Replace :",
            "A/C Filters Cover, Caps, Screws :",
            "Foam Around Compressor :",
            "Stairs/ Hallway :",
            "Outlets :",
            "Covers :",
            "Service Covers :",
            "Lights :",
            "Light Bulbs Needed :",
            "Floor Covering :",
            "Hand Rails :",
            "Walls :",
            "Blinds :",
        )

        val mis = arrayListOf(
            KitchenFormItem("Cleaning", "", ""),
            KitchenFormItem("Tennent Personal Belongings :", "", ""),
            KitchenFormItem("Furniture :", "", ""),
            KitchenFormItem("Hire Outside Company For Deep Cleaning :", "", ""),
            KitchenFormItem("Smoke Detectors (One In Each Bedroom, Hall, And Living Room)", "", ""),
            KitchenFormItem("Batteries In Smoke Detectors: Count", "", ""),
            KitchenFormItem(
                "Smoke Detectors Replace Because They Don’t Work Or Are Missing: Count :",
                "",
                ""
            ),
            KitchenFormItem("C/O In Each Hall :", "", ""),
            KitchenFormItem("HAVC", "", ""),
            KitchenFormItem("A/C (Requires Professional Diagnoses) :", "", ""),
            KitchenFormItem("A/C Vents: Replace :", "", ""),
            KitchenFormItem("A/C Filters: Replace :", "", ""),
            KitchenFormItem("A/C Filters Cover, Caps, Screws :", "Repair", "Replace"),
            KitchenFormItem("Foam Around Compressor :", "Repair", "Replace"),
            KitchenFormItem("Stairs/ Hallway", "", ""),
            KitchenFormItem("Outlets :", "", ""),
            KitchenFormItem("Covers :", "", ""),
            KitchenFormItem("Service Covers :", "", ""),
            KitchenFormItem("Lights :", "", ""),
            KitchenFormItem("Light Bulbs Needed :", "", ""),
            KitchenFormItem("Floor Covering :", "", ""),
            KitchenFormItem("Hand Rails :", "", ""),
            KitchenFormItem("Walls :", "", ""),
            KitchenFormItem("Blinds :", "W", "H"),
        )

        val livingRoomItems = arrayListOf(
            LivingRoomFormItem("Vent Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Carpet And Flooring", "", "", "", "", ""),
            LivingRoomFormItem("Carpet :", "Clean", "Replace", "If Replace, Size", "", ""),
            LivingRoomFormItem("Loose Or Cracked Tiles Or Damaged Floor :", "", "", "", "", ""),
            LivingRoomFormItem("Walls/ Baseboards/ Ceiling", "", "", "", "", ""),
            LivingRoomFormItem("Paint :", "Touch Up", "Whole Wall", "", "", ""),
            LivingRoomFormItem("Drywall Damage :", "", "", "", "", ""),
            LivingRoomFormItem("Remove / Repair Nails, Hangers, Etc :", "", "", "", "", ""),
            LivingRoomFormItem("Evidence Of Water Damage :", "Wall", "Ceiling", "", "", ""),
            LivingRoomFormItem("Windows", "", "", "", "", ""),
            LivingRoomFormItem("Open And Close Easily :", "", "", "", "", ""),
            LivingRoomFormItem(
                "Screens (Each Window And Sliding Glass Door) :",
                "",
                "",
                "",
                "",
                ""
            ),
            LivingRoomFormItem("Locking Device :", "", "", "", "", ""),
            LivingRoomFormItem("Broken, Cracked Or Missing :", "", "", "", "", ""),
            LivingRoomFormItem("Blinds :", "W", "H", "Color :", "Style : ", "Wand :"),
            LivingRoomFormItem("Condensation Between Window Panes :", "", "", "", "", ""),
            LivingRoomFormItem("Electrical", "", "", "", "", ""),
            LivingRoomFormItem("Lights :", "", "", "", "", ""),
            LivingRoomFormItem("Light Bulbs Needed :", "", "", "", "", ""),
            LivingRoomFormItem("Ceiling Fans :", "", "", "", "", ""),
            LivingRoomFormItem("Outlets :", "", "", "", "", ""),
            LivingRoomFormItem("Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Service Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Closets", "", "", "", "", ""),
            LivingRoomFormItem("Closet Door Guide/Track, Handles :", "", "", "", "", ""),
            LivingRoomFormItem("Closet Door Open And Close :", "", "", "", "", ""),
            LivingRoomFormItem("Racks, Rods, Shelves :", "", "", "", "", ""),
            LivingRoomFormItem("Fireplace", "", "", "", "", ""),
            LivingRoomFormItem("Missing Key :", "", "", "", "", "")
        )

        val LaundryRoomItems = arrayListOf(
            "Clothes Washer :",
            "Washer Shut Off Valves :",
            "Hose And Supply Line :",
            "Dryer :",
            "Lint Screen :",
            "Lint Path :",
            "Walls/ Damage :",
            "Doors :",
            "Door Locks/Knobs :",
            "Door Stops :",
            "Missing Hardware On Doors :",
            "Lights :",
            "Light Bulbs Needed :",
            "Vent Fans :",
            "Outlets :",
            "Covers :",
            "Service Covers :",
            "Cabinet Doors and Handles :",
            "Cabinet Drawers :",
            "Shelves :",

            )

        val greatRoomItems = arrayListOf(
            LivingRoomFormItem("Vent Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Carpet And Flooring", "", "", "", "", ""),
            LivingRoomFormItem("Carpet :", "Clean", "Replace", "If Replace, Size", "", ""),
            LivingRoomFormItem("Loose Or Cracked Tiles Or Damaged Floor :", "", "", "", "", ""),
            LivingRoomFormItem("Walls/ Baseboards/ Ceiling", "", "", "", "", ""),
            LivingRoomFormItem("Paint :", "Touch Up", "Whole Wall", "", "", ""),
            LivingRoomFormItem("Drywall Damage :", "", "", "", "", ""),
            LivingRoomFormItem("Remove / Repair Nails, Hangers, Etc :", "", "", "", "", ""),
            LivingRoomFormItem("Evidence Of Water Damage :", "Wall", "Ceiling", "", "", ""),
            LivingRoomFormItem("Windows/ Exterior Doors", "", "", "", "", ""),
            LivingRoomFormItem("Open And Close Easily :", "", "", "", "", ""),
            LivingRoomFormItem(
                "Screens (Each Window And Sliding Glass Door) :",
                "",
                "",
                "",
                "",
                ""
            ),
            LivingRoomFormItem("Locking Device :", "", "", "", "", ""),
            LivingRoomFormItem("Broken, Cracked Or Missing :", "", "", "", "", ""),
            LivingRoomFormItem("Blinds :", "W", "H", "Color :", "Style : ", "Wand :"),
            LivingRoomFormItem("Exterior Door Security Locking Device :", "", "", "", "", ""),
            LivingRoomFormItem("Condensation Between Window Panes :", "", "", "", "", ""),
            LivingRoomFormItem("Doors", "", "", "", "", ""),
            LivingRoomFormItem("Doors :", "", "", "", "", ""),
            LivingRoomFormItem("Astragal :", "", "", "", "", ""),
            LivingRoomFormItem("Door Locks/ Knobs :", "", "", "", "", ""),
            LivingRoomFormItem("Missing Hardware On Doors :", "", "", "", "", ""),
            LivingRoomFormItem("Door Stops :", "", "", "", "", ""),
            LivingRoomFormItem("Electrical", "", "", "", "", ""),
            LivingRoomFormItem("Lights :", "", "", "", "", ""),
            LivingRoomFormItem("Light Bulbs Needed :", "", "", "", "", ""),
            LivingRoomFormItem("Ceiling Fans :", "", "", "", "", ""),
            LivingRoomFormItem("Outlets :", "", "", "", "", ""),
            LivingRoomFormItem("Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Service Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Closets", "", "", "", "", ""),
            LivingRoomFormItem("Closet Door Guide/Track, Handles :", "", "", "", "", ""),
            LivingRoomFormItem("Closet Door Open And Close :", "", "", "", "", ""),
            LivingRoomFormItem("Racks, Rods, Shelves :", "", "", "", "", ""),
            LivingRoomFormItem("Fireplace", "", "", "", "", ""),
            LivingRoomFormItem("Missing Key :", "", "", "", "", "")
        )

        val dining = arrayListOf(
            LivingRoomFormItem("Vent Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Carpet And Flooring", "", "", "", "", ""),
            LivingRoomFormItem("Carpet :", "Clean", "Replace", "If Replace, Size", "", ""),
            LivingRoomFormItem("Loose Or Cracked Tiles Or Damaged Floor :", "", "", "", "", ""),
            LivingRoomFormItem("Walls/ Baseboards/ Ceiling", "", "", "", "", ""),
            LivingRoomFormItem("Paint :", "Touch Up", "Whole Wall", "", "", ""),
            LivingRoomFormItem("Drywall Damage :", "", "", "", "", ""),
            LivingRoomFormItem("Remove / Repair Nails, Hangers, Etc :", "", "", "", "", ""),
            LivingRoomFormItem("Evidence Of Water Damage :", "Wall", "Ceiling", "", "", ""),
            LivingRoomFormItem("Windows/ Exterior Doors", "", "", "", "", ""),
            LivingRoomFormItem("Open And Close Easily :", "", "", "", "", ""),
            LivingRoomFormItem(
                "Screens (Each Window And Sliding Glass Door) :",
                "",
                "",
                "",
                "",
                ""
            ),
            LivingRoomFormItem("Locking Device :", "", "", "", "", ""),
            LivingRoomFormItem("Broken, Cracked Or Missing :", "", "", "", "", ""),
            LivingRoomFormItem("Blinds :", "W", "H", "Color :", "Style : ", "Wand :"),
            LivingRoomFormItem("Exterior Door Security Locking Device :", "", "", "", "", ""),
            LivingRoomFormItem("Condensation Between Window Panes :", "", "", "", "", ""),
            LivingRoomFormItem("Doors", "", "", "", "", ""),
            LivingRoomFormItem("Doors :", "", "", "", "", ""),
            LivingRoomFormItem("Astragal :", "", "", "", "", ""),
            LivingRoomFormItem("Door Locks/ Knobs :", "", "", "", "", ""),
            LivingRoomFormItem("Missing Hardware On Doors :", "", "", "", "", ""),
            LivingRoomFormItem("Door Stops :", "", "", "", "", ""),
            LivingRoomFormItem("Electrical", "", "", "", "", ""),
            LivingRoomFormItem("Lights :", "", "", "", "", ""),
            LivingRoomFormItem("Light Bulbs Needed :", "", "", "", "", ""),
            LivingRoomFormItem("Ceiling Fans :", "", "", "", "", ""),
            LivingRoomFormItem("Outlets :", "", "", "", "", ""),
            LivingRoomFormItem("Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Service Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Fireplace", "", "", "", "", ""),
            LivingRoomFormItem("Missing Key :", "", "", "", "", "")
        )


        val frontDoorItems = arrayListOf(
            "Doors :",
            "Astragal :",
            "Door Bell :",
            "Door Locks: ",
            "Door Knobs :",
            "Sagging Hinges :",
            "Strike Plates :",
            "Missing Hardware On Doors :",
            "Peep Hole (Height 60” From The Bottom Of Door) :",
            "Door Stops :",
            "Weather Stripping :"
        )

        val DiningRoomItems = arrayListOf(
            "Vent Covers :",
            "Carpet And Flooring :",
            "Carpet :",
            "Loose Or Cracked Tiles Or Damaged Floor :",
            "Walls/ Baseboards/ Ceiling :",
            "Paint :",
            "Drywall Damage :",
            "Remove / Repair Nails, Hangers, Etc :",
            "Evidence Of Water Damage :",
            "Windows/ Exterior Doors :",
            "Open And Close Easily :",
            "Screens (Each Window And Sliding Glass Door) :",
            "Locking Device :",
            "Broken, Cracked Or Missing :",
            "Blinds :",
            "Exterior Door Security Locking Device :",
            "Doors",
            "Doors :",
            "Astragal :",
            "Door Locks/ Knobs :",
            "Missing Hardware On Doors :",
            "Door Stops :",
            "Electrical",
            "Lights :",
            "Light Bulbs Needed :",
            "Ceiling Fans:",
            "Outlets :",
            "Covers :",
            "Service Covers :",
            "Closets :",
            "Closet Door Guide/Track, Handles :",
            "Closet Door Open And Close :",
            "Racks, Rods, Shelves :",
            "Fireplace :",
            "Missing Key :"

        )

        val bed = arrayListOf(
            LivingRoomFormItem("Vent Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Carpet And Flooring", "", "", "", "", ""),
            LivingRoomFormItem("Carpet :", "Clean", "Replace", "If Replace, Size", "", ""),
            LivingRoomFormItem("Loose Or Cracked Tiles Or Damaged Floor :", "", "", "", "", ""),
            LivingRoomFormItem("Walls/ Baseboards/ Ceiling", "", "", "", "", ""),
            LivingRoomFormItem("Paint :", "Touch Up", "Full Wall", "", "", ""),
            LivingRoomFormItem("Drywall Damage :", "", "", "", "", ""),
            LivingRoomFormItem("Remove / Repair Nails, Hangers, Etc :", "", "", "", "", ""),
            LivingRoomFormItem("Evidence Of Water Damage :", "Wall", "Ceiling", "", "", ""),
            LivingRoomFormItem("Windows/ Sliding Glass Doors", "", "", "", "", ""),
            LivingRoomFormItem("Open And Close Easily :", "", "", "", "", ""),
            LivingRoomFormItem(
                "Screens (Each Window And Sliding Glass Doors) :",
                "",
                "",
                "",
                "",
                ""
            ),
            LivingRoomFormItem("Locking Device :", "", "", "", "", ""),
            LivingRoomFormItem("Broken, Cracked Or Missing :", "", "", "", "", ""),
            LivingRoomFormItem("Condensation Between Panes :", "", "", "", "", ""),
            LivingRoomFormItem("Blinds :", "W", "H", "Color :", "Style : ", "Wand :"),
            LivingRoomFormItem("Exterior Door Security Locking Device :", "", "", "", "", ""),
            LivingRoomFormItem("Doors", "", "", "", "", ""),
            LivingRoomFormItem("Doors :", "", "", "", "", ""),
            LivingRoomFormItem("Astragal :", "", "", "", "", ""),
            LivingRoomFormItem("Door Locks/ Knobs :", "", "", "", "", ""),
            LivingRoomFormItem("Missing Hardware On Doors :", "", "", "", "", ""),
            LivingRoomFormItem("Door Stops :", "", "", "", "", ""),
            LivingRoomFormItem("Electrical", "", "", "", "", ""),
            LivingRoomFormItem("Lights :", "", "", "", "", ""),
            LivingRoomFormItem("Light Bulbs :", "", "", "", "", ""),
            LivingRoomFormItem("Ceiling Fans :", "", "", "", "", ""),
            LivingRoomFormItem("Outlets :", "", "", "", "", ""),
            LivingRoomFormItem("Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Service Covers :", "", "", "", "", ""),
            LivingRoomFormItem("Closets", "", "", "", "", ""),
            LivingRoomFormItem("Closet Door Pins, Gliders, Handles :", "", "", "", "", ""),
            LivingRoomFormItem("Closet Door Open And Close :", "", "", "", "", ""),
            LivingRoomFormItem("Racks, Rods, Shelves :", "", "", "", "", "")
        )
        val garageRoomItems = arrayListOf(
            GarageFormItem("Garage Door Works :", "", ""),
            GarageFormItem("Garage Door Opener :", "", ""),
            GarageFormItem("Garage Door To House Self-Closing Hinges :", "", ""),
            GarageFormItem("Locks/ Knobs :", "", ""),
            GarageFormItem("Cabinets :", "", ""),
            GarageFormItem("Electrical", "", ""),
            GarageFormItem("Lights :", "", ""),
            GarageFormItem("Light Bulbs Needed :", "", ""),
            GarageFormItem("Ceiling Fans :", "", ""),
            GarageFormItem("Outlets :", "", ""),
            GarageFormItem("Covers :", "", ""),
            GarageFormItem("Service Covers :", "", ""),
            GarageFormItem("Lighting :", "", ""),
            GarageFormItem("Power Wash Floor :", "", ""),
            GarageFormItem("Water Softener Removal :", "", ""),
            GarageFormItem("Water Heater", "", ""),
            GarageFormItem("Hot Water Heater Earthquake Straps :", "", ""),
            GarageFormItem(
                "Hot Water Heater Collection Pan With Hose Leading To Outside Overflow :",
                "",
                ""
            ),
            GarageFormItem("Drain To Outside :", "", ""),
            GarageFormItem("TPRV Flows Down :", "", ""),
            GarageFormItem("Walls/ Baseboards/ Ceiling", "", ""),
            GarageFormItem("Paint :", "Touch Up", "Full Wall"),
            GarageFormItem("Drywall Damage :", "", ""),
            GarageFormItem("Remove / Repair Nails, Hangers, Etc :", "", ""),
            GarageFormItem("Evidence Of Water Damage :", "Wall", "Ceiling"),

            )

        val bathroomItemsList = arrayListOf(
            "Towel Bars :",
            "Toilet Paper Holders :",
            "GFCI And Outlets :",
            "Lights Switch And Fan Switch :",
            "Exhaust Fan Vent :",
            "Light Bulb Replace :",
            "Shower/Tub",
            "Shower Head :",
            "Shower Handle :",
            "Shower/Tub Doors :",
            "Water Drain :",
            "Tub Stoppers :",
            "Grout And Caulk In Bathroom :",
            "Shower Diverter :",
            "Sink",
            "Shelves And Drawers :",
            "Medicine Cabinet :",
            "Shelves :",
            "Grout And Calking :",
            "Faucet :",
            "Aerators :",
            "Water Drain :",
            "Sink Stoppers :",
            "Water Leak Or Damage Under Sink :",
            "Toilet",
            "Toilet Leaks Or Obstructions :",
            "Toilet Seats :",
            "Caulk Around Base Of Toilet :",
            "Flush Valve And Fill Valve :",
            "Toilet Loose At Floor :",
            "Doors",
            "Doors :",
            "Astragal :",
            "Door Locks/Knobs :",
            "Door Stops /Missing Hardware On Doors :",
        )

        val kitchenItems = arrayListOf(
            KitchenFormItem("Grout And Caulk In Kitchen :", "", ""),
            KitchenFormItem("Cabinet Doors And Handles :", "", ""),
            KitchenFormItem("Shelves :", "", ""),
            KitchenFormItem("Cabinet Drawers :", "", ""),
            KitchenFormItem("Filter In Vent Hood :", "", ""),
            KitchenFormItem("Hood Light And Fan :", "", ""),
            KitchenFormItem("Vent Covers :", "", ""),
            KitchenFormItem("Island :", "", ""),
            KitchenFormItem("Electrical", "", ""),
            KitchenFormItem("Lights :", "", ""),
            KitchenFormItem("Light Bulbs Needed :", "", ""),
            KitchenFormItem("Ceiling Fans :", "", ""),
            KitchenFormItem("Outlets :", "", ""),
            KitchenFormItem("Covers :", "", ""),
            KitchenFormItem("Service Covers :", "", ""),
            KitchenFormItem("Sink", "", ""),
            KitchenFormItem("Faucets :", "", ""),
            KitchenFormItem("Garbage Disposal :", "", ""),
            KitchenFormItem("Air Gap Cap :", "", ""),
            KitchenFormItem("Guard :", "", ""),
            KitchenFormItem("Under Sinks Leak :", "", ""),
            KitchenFormItem("Damage Under Sink :", "", ""),
            KitchenFormItem(
                "Appliances (Check For Filters, Knobs, Racks, Shelves, Drip Pans, Light Bulbs) Take Condition Photos And Modal Number For All Appliances",
                "",
                ""
            ),
            KitchenFormItem("Refrigerator :", "Drawers ", "Shelves "),
            KitchenFormItem("Oven :", "", ""),
            KitchenFormItem("Dishwasher :", " Inside Racks", ""),
            KitchenFormItem("Microwave :", "", ""),
            KitchenFormItem("Pantry", "", ""),
            KitchenFormItem("Pantry Door Guide/Track, Handles :", "", ""),
            KitchenFormItem("Pantry Door Open And Close :", "", ""),
            KitchenFormItem("Racks, Rods, Shelves :", "", ""),
            KitchenFormItem("Windows", "", ""),
            KitchenFormItem("Open And Close Easily :", "", ""),
            KitchenFormItem(
                "Screens (Each Window And Sliding Glass Door) :",
                "",
                "",
            ),
            KitchenFormItem("Locking Device :", "", ""),
            KitchenFormItem("Broken, Cracked Or Missing :", "", ""),
            KitchenFormItem("Blinds :", "", ""),
            KitchenFormItem("Condensation Between Window Panes :", "", "")
        )
    }
}


class LivingRoomFormItem(
    val itemName: String, val itemCondition1: String,
    val itemCondition2: String, val itemDetail1: String,
    val itemDetail2: String, val itemDetail3: String
)

class GarageFormItem(
    val itemName: String, val itemCondition1: String,
    val itemCondition2: String
)


class KitchenFormItem(
    val itemName: String, val itemCondition1: String,
    val itemCondition2: String
)

class CommonFromItem(
    val itemName: String, val itemCondition1: String,
    val itemCondition2: String, val itemDetail1: String,
    val itemDetail2: String, val itemDetail3: String
)