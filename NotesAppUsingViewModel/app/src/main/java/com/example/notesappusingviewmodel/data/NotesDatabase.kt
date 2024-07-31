package com.example.notes_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes_app.datamodel.Notes

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun NotesDao():NotesDao

    companion object{

        @Volatile
        private var INSTANCE: NotesDatabase?=null

        fun getDatabase(context: Context): NotesDatabase {
            val tempInstance= INSTANCE
            if(tempInstance != null)
            {
                return tempInstance
            }
            synchronized(this){
                val instance=Room.databaseBuilder(
                    context.applicationContext, NotesDatabase::class.java,
                    "notes_DataBase"
                ).build()
                INSTANCE =instance
                return instance
            }

        }
    }



}