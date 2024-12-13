package com.example.truckmanagement.Data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "entiteti")
data class Ent (
    //@PrimaryKey val TipKamiona :String,
    @ColumnInfo(name = "TipKamiona") val TipKamiona: String,
    @ColumnInfo(name = "Gorivo") val Gorivo: Int,
    @ColumnInfo(name = "Odrzavanje") val Odrzavanje: Int,
    @ColumnInfo(name = "Putarina") val Putarina: Int,
    @ColumnInfo(name = "Datum") val Datum: String,

    @PrimaryKey(autoGenerate = true) var id: Int = 0

)

