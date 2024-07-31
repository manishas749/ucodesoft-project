package com.example.paginglibrary.models

import androidx.room.TypeConverter
import com.google.gson.Gson

class RoomTypeConvertor {

        @TypeConverter
        fun sourceToString(source: Source): String {
            return Gson().toJson(source)
        }

        @TypeConverter
        fun stringToSource(str: String): Source {
            return Gson().fromJson(str, Source::class.java)
        }


}