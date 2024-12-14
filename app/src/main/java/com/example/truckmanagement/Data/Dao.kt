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

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/%/2024'")
        fun getUkupniTroskoviZaKamion(tipKamiona: String):Int

        //Decembar------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '12/%/2024'")
        fun GorivoDecembar(tipKamiona: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '12/%/' || :godina")
        fun OdrzavanjeDecembar(tipKamiona: String, godina :String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '12/%/2024'")
        fun PutarinaDecembar(tipKamiona: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '12/%/' || :godina")
        fun UkupnoDecembar(tipKamiona: String, godina: String):Int
        //Januar-------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '01/%/' || :godina")
        fun JanuarGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '01/%/' || :godina")
        fun JanuarOdrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '01/%/' || :godina")
        fun JanuarPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '01/%/' || :godina")
        fun JanuarUkupno(tipKamiona: String, godina: String):Int
        //Februar------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '02/%/' || :godina")
        fun FebruarGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '02/%/' || :godina")
        fun FebruarOdrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '02/%/' || :godina")
        fun FebruarPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '02/%/' || :godina")
        fun FebruarUkupno(tipKamiona: String, godina: String):Int

}