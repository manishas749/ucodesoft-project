package com.rentreadychecklist.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.model.bathroom.Bathroom
import com.rentreadychecklist.model.bedrooms.Bedroom
import com.rentreadychecklist.model.diningRoom.DiningRoom
import com.rentreadychecklist.model.frontdoors.FrontDoors
import com.rentreadychecklist.model.garage.Garage
import com.rentreadychecklist.model.garage.GarageDoorsOk
import com.rentreadychecklist.model.garage.GarageFix
import com.rentreadychecklist.model.garage.GarageItemList
import com.rentreadychecklist.model.greatroom.GreatRoom
import com.rentreadychecklist.model.homescreen.HomeScreen
import com.rentreadychecklist.model.imageupload.ImageUploadCommon
import com.rentreadychecklist.model.kitchen.Kitchen
import com.rentreadychecklist.model.laundry.LaundryRoom
import com.rentreadychecklist.model.livingRoom.LivingRoom
import com.rentreadychecklist.model.miscellaneous.Miscellaneous
import com.rentreadychecklist.model.oldmodel.garageold1.GarageOld
import com.rentreadychecklist.model.outside.Outside


@Database(entities = [AppData::class], version = 2, exportSchema = true)
@TypeConverters(
    FormTypeConvertorHomeScreen::class,
    FormTypeConvertorOutside::class,
    FormTypeConvertorFrontDoors::class,
    FormTypeConvertorGarage::class,
    FormTypeConvertorLaundryRoom::class,
    FormTypeConvertorLivingRoom::class,
    FormTypeConvertorGreatRoom::class,
    FormTypeConvertorDiningRoom::class,
    FormTypeConvertorKitchen::class,
    FormTypeConvertorMiscellaneous::class,
    FormTypeConvertorBedrooms::class,
    FormTypeConvertorBathroom::class
)

abstract class RoomAppDb : RoomDatabase() {


    abstract fun userDao(): AppDao?

    companion object {

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                    "CREATE TABLE users_new(id INTEGER NOT NULL, HomeScreen TEXT NOT NULL, Outside TEXT NOT NULL, FrontDoors TEXT NOT NULL, Garage TEXT NOT NULL,LaundryRoom TEXT NOT NULL, LivingRoom TEXT NOT NULL, GreatRoom TEXT NOT NULL, DiningRoom TEXT NOT NULL, Kitchen TEXT NOT NULL, Miscellaneous TEXT NOT NULL, Bedroom TEXT NOT NULL, Bathroom TEXT NOT NULL, PRIMARY KEY('id'))"
                )
                // Copy the data
                database.execSQL(
                    "INSERT INTO users_new SELECT * FROM formInformation"
                )
                // Remove the old table
                database.execSQL("DROP TABLE formInformation")
                // Change the table name to the correct one
                database.execSQL("ALTER TABLE users_new RENAME TO formInformation")
            }
        }

        @Volatile
        private var INSTANCE: RoomAppDb? = null

        fun getAppDatabase(context: Context): RoomAppDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {

                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, RoomAppDb::class.java, "AppDB"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}

class FormTypeConvertorHomeScreen {
    @TypeConverter
    fun listToJsonString(value: List<HomeScreen>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<HomeScreen>::class.java).toList()

}


class FormTypeConvertorOutside {
    @TypeConverter
    fun listToJsonString(value: List<Outside>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<Outside>::class.java).toList()

}

class FormTypeConvertorFrontDoors {
    @TypeConverter
    fun listToJsonString(value: List<FrontDoors>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<FrontDoors>::class.java).toList()

}

class FormTypeConvertorGarage {
    @TypeConverter
    fun listToJsonString(value: List<Garage>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?): List<Garage> {

        val arrayList = arrayListOf<Garage>()
        if (value!!.contains("ItemCondition")) {
            val l = Gson().fromJson(value, Array<Garage>::class.java).toList()
            for (element in l) {
                arrayList.add(element)
            }
        } else {
            val l = Gson().fromJson(value, Array<GarageOld>::class.java).toList()
            for (i in l.indices) {
                val item = Garage(
                    GarageItemList("", false, false), l[i].ok,
                    l[i].na, l[i].fix
                )
                arrayList.add(item)
            }
            val arrayListSize = arrayList.size
            val itemSize = FormItemList.garageRoomItems.size

            if (arrayListSize != itemSize) {
                for (i in 0 until FormItemList.garageRoomItems.size - arrayList.size) {
                    val item = Garage(
                        GarageItemList("", false, false),
                        GarageDoorsOk("", arrayListOf<ImageUploadCommon>()),
                        "",
                        GarageFix("", "", "", "", arrayListOf<ImageUploadCommon>())
                    )
                    arrayList.add(item)
                }
            }

        }

        return arrayList
    }


}

class FormTypeConvertorLaundryRoom {
    @TypeConverter
    fun listToJsonString(value: List<LaundryRoom>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<LaundryRoom>::class.java).toList()

}

class FormTypeConvertorLivingRoom {
    @TypeConverter
    fun listToJsonString(value: List<LivingRoom>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<LivingRoom>::class.java).toList()

}

class FormTypeConvertorGreatRoom {
    @TypeConverter
    fun listToJsonString(value: List<GreatRoom>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<GreatRoom>::class.java).toList()

}

class FormTypeConvertorDiningRoom {
    @TypeConverter
    fun listToJsonString(value: List<DiningRoom>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<DiningRoom>::class.java).toList()

}

class FormTypeConvertorKitchen {
    @TypeConverter
    fun listToJsonString(value: List<Kitchen>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<Kitchen>::class.java).toList()

}

class FormTypeConvertorMiscellaneous {
    @TypeConverter
    fun listToJsonString(value: List<Miscellaneous>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<Miscellaneous>::class.java).toList()

}

class FormTypeConvertorBedrooms {
    @TypeConverter
    fun listToJsonString(value: List<Bedroom>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<Bedroom>::class.java).toList()

}

class FormTypeConvertorBathroom {
    @TypeConverter
    fun listToJsonString(value: List<Bathroom>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) =
        Gson().fromJson(value, Array<Bathroom>::class.java).toList()

}


