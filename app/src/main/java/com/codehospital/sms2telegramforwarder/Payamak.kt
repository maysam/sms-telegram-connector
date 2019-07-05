package com.codehospital.sms2telegramforwarder

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Payamak(@PrimaryKey(autoGenerate = true) var uid: Int = 0) {

    var from_phone: String? = null
    var from_person: String? = null
    var text: String? = null
    var sentToTelegram: Boolean = false

//    @Ignore    var picture: Bitmap? = null
}
