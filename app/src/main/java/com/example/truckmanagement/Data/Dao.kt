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

        @Query("SELECT * FROM entiteti WHERE TipKamiona = :tipKamiona AND Id_Transporta = :iD")
        fun getTroskoviZaKamion(tipKamiona:String, iD:String): List<Ent>

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona")
        fun getUkupniTroskoviZaKamion(tipKamiona: String):Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/%/' || :godina")
        fun UkupniTroskoviGodina(tipKamiona: String, godina: String):Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Id_Transporta = :id")
        fun TroskoviPuta(tipKamiona: String, id:Int):Int

        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona= :tipKamiona AND Datum LIKE '%/%' || :godina")
        fun GorivoGodisnje(tipKamiona: String,godina: String):Int
        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona= :tipKamiona AND Datum LIKE '%/%' || :godina")
        fun OdrzavanjeGodisnje(tipKamiona: String,godina: String):Int
        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona= :tipkamiona AND Datum LIKE '%/%' || :godina")
        fun PutarinaGodisnje(tipkamiona: String,godina: String):Int

        //Decembar------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/12/' || :godina")
        fun GorivoDecembar(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/12/' || :godina")
        fun OdrzavanjeDecembar(tipKamiona: String, godina :String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/12/' || :godina")
        fun PutarinaDecembar(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/12/' || :godina")
        fun UkupnoDecembar(tipKamiona: String, godina: String):Int

        //Januar-------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/01/' || :godina")
        fun JanuarGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/01/' || :godina")
        fun JanuarOdrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/01/' || :godina")
        fun JanuarPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/01/' || :godina")
        fun JanuarUkupno(tipKamiona: String, godina: String):Int

        //Februar------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/02/' || :godina")
        fun FebruarGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/02/' || :godina")
        fun FebruarOdrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/02/' || :godina")
        fun FebruarPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/02/' || :godina")
        fun FebruarUkupno(tipKamiona: String, godina: String):Int

        //Mart------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/03/' || :godina")
        fun MartGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/03/' || :godina")
        fun MartOdrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/03/' || :godina")
        fun MartPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/03/' || :godina")
        fun MartUkupno(tipKamiona: String, godina: String):Int

        //April------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/04/' || :godina")
        fun AprilGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/04/' || :godina")
        fun Aprildrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/04/' || :godina")
        fun AprilPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/04/' || :godina")
        fun AprilUkupno(tipKamiona: String, godina: String):Int

        //Maj------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/05/' || :godina")
        fun MajGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/05/' || :godina")
        fun Majdrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/05/' || :godina")
        fun MajPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/05/' || :godina")
        fun MajUkupno(tipKamiona: String, godina: String):Int

        //Jun------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/06/' || :godina")
        fun JunGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/06/' || :godina")
        fun Jundrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/06/' || :godina")
        fun JunPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/06/' || :godina")
        fun JunUkupno(tipKamiona: String, godina: String):Int

        //Jul------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/07/' || :godina")
        fun JulGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/07/' || :godina")
        fun Juldrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/07/' || :godina")
        fun JulPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/07/' || :godina")
        fun JulUkupno(tipKamiona: String, godina: String):Int

        //Avgust------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/08/' || :godina")
        fun AvgustGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/08/' || :godina")
        fun Avgustdrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/08/' || :godina")
        fun AvgustPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/08/' || :godina")
        fun AvgustUkupno(tipKamiona: String, godina: String):Int

        //Septembar------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/09/' || :godina")
        fun SeptembarGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/09/' || :godina")
        fun Septembardrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/09/' || :godina")
        fun SeptembarPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/09/' || :godina")
        fun SeptembarUkupno(tipKamiona: String, godina: String):Int

        //Oktobar------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/10/' || :godina")
        fun OktobarGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/10/' || :godina")
        fun Oktobardrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/10/' || :godina")
        fun OktobarPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/10/' || :godina")
        fun OktobarUkupno(tipKamiona: String, godina: String):Int

        //Novembar------------------------------
        @Query("SELECT SUM(Gorivo) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/11/' || :godina")
        fun NovembarGorivo(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Odrzavanje) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/11/' || :godina")
        fun Novembardrzavanje(tipKamiona: String, godina: String) : Int

        @Query("SELECT SUM(Putarina) FROM entiteti WHERE TipKamiona=:tipKamiona AND Datum LIKE '%/11/' || :godina")
        fun NovembarPutarina(tipKamiona: String, godina: String) : Int

        @Query("SELECT (SUM(Gorivo)+SUM(Putarina)+SUM(Odrzavanje)) FROM entiteti WHERE TipKamiona = :tipKamiona AND Datum LIKE '%/11/' || :godina")
        fun NovembarUkupno(tipKamiona: String, godina: String):Int

}