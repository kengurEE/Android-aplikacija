package com.example.truckmanagement.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface Dao {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        fun insertTrosak(vararg ent: Ent)

        @Delete
        fun deleteTrosak(ent: Ent)

        @Query("SELECT * FROM entiteti WHERE TipKamiona = :tipKamiona")
        fun getTroskoviZaKamion(tipKamiona:String): List<Ent>

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona")
        fun getUkupniTroskoviZaKamion(tipKamiona: String):Int


}