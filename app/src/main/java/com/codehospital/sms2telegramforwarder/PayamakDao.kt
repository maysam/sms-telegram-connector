package com.codehospital.sms2telegramforwarder

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

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

    @Insert
    fun insert(payamak: Payamak)
}
