package com.codehospital.sms2telegramforwarder

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Dao
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.*
import android.arch.persistence.room.Room

@Dao
interface PayamakDao {
    @get:Query("SELECT * FROM payamak")
    val all: List<Payamak>

    @Query("SELECT * FROM payamak WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Payamak>

    @Query("SELECT * FROM payamak WHERE from_phone LIKE :first AND " + "from_person LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Payamak

    @Insert
    fun insertAll(vararg payamaks: Payamak)

    @Delete
    fun delete(payamak: Payamak)
}
