package com.example.truckmanagement.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao2Transport {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertTransport(vararg ent2: Ent2)

    @Query("SELECT * FROM transport WHERE TipKamiona = :tipKamiona")
    fun TransportPodaci(tipKamiona:String): List<Ent2>

}