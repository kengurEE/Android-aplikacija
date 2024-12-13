package com.example.truckmanagement.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Ent::class], version = 3, exportSchema = false)
abstract class BazaPodataka : RoomDatabase()  {

    abstract fun  dao():Dao
}