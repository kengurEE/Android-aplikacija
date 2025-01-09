package com.example.truckmanagement.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Ent::class, Ent2::class, EntKilometraza::class], version = 10, exportSchema = false)
abstract class BazaPodataka : RoomDatabase()  {
    abstract fun dao():Dao
    abstract fun dao2transport():Dao2Transport
    abstract fun daoKilometraza():DaoKilometraza
}