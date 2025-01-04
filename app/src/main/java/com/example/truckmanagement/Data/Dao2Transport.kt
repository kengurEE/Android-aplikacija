package com.example.truckmanagement.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface Dao2Transport {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertTransport(vararg ent2: Ent2)

    @Query("SELECT * FROM transport WHERE TipKamiona = :tipKamiona")
    fun TransportPodaci(tipKamiona:String): List<Ent2>

    @Query("SELECT * FROM transport WHERE TipKamiona = :tipKamiona AND Zavrsi==false")
    fun AktuelneRute(tipKamiona:String): List<Ent2>

    @Query("SELECT * FROM transport WHERE TipKamiona = :tipKamiona AND Zavrsi==true")
    fun ZavrsenTransport(tipKamiona: String): List<Ent2>

    @Update
    fun update(ent2: Ent2)

}