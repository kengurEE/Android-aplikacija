package com.example.truckmanagement.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoKilometraza {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun Insert(vararg kilometraza: EntKilometraza)

    @Query("SELECT Kilometraza FROM kilometraza WHERE TipKilometraze = :tipkm ORDER BY id_kilometraze DESC LIMIT 1")
    fun PoslednjiOdradjenServis(tipkm:String) : Int

}