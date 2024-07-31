package com.example.fragmentsproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fragmentsproject.dao.EmployeeDataDao
import com.example.fragmentsproject.data.EmployeeData

@Database(entities = [EmployeeData::class], version = 1, exportSchema = false)
abstract class EmployeeDetailDatabase: RoomDatabase() {

    abstract fun EmployeeDataDao():EmployeeDataDao

    companion object{

        @Volatile
        private var INSTANCE: EmployeeDetailDatabase?=null

        fun getDatabase(context: Context): EmployeeDetailDatabase {
            val tempInstance= INSTANCE
            if(tempInstance != null)
            {
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext, EmployeeDetailDatabase::class.java,
                    "emp_DataBase"
                ).build()
                INSTANCE =instance
                return instance
            }

        }
    }



}