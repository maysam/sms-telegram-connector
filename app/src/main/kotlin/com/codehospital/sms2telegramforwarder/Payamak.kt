package com.codehospital.sms2telegramforwarder

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Dao
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.*
import android.arch.persistence.room.Room

@Entity
class Payamak {
    @PrimaryKey
    var uid: Int = 0

    var from_phone: String? = null
    var from_person: String? = null
    var text: String? = null
    var sentToTelegram: Boolean = false

//    @Ignore    var picture: Bitmap? = null
}
