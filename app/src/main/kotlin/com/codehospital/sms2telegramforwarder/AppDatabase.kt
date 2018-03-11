package com.codehospital.sms2telegramforwarder

import android.content.Context;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = arrayOf(Payamak::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun payamakDao(): PayamakDao


//    private var INSTANCE: AppDatabase? = null
//
//    fun getDatabase(context: Context): AppDatabase {
//        if (INSTANCE == null) {
//            INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java!!, "sms-database-name")
//                    //Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
//                    // To simplify the exercise, allow queries on the main thread.
//                    // Don't do this on a real app!
//                    .allowMainThreadQueries()
//                    // recreate the database if necessary
//                    .fallbackToDestructiveMigration()
//                    .build()
//        }
//        return INSTANCE!!
//    }
//
//    fun destroyInstance() {
//        INSTANCE = null
//    }
}
