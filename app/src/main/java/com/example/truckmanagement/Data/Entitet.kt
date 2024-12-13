package com.example.truckmanagement.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "troskovi")
data class Entitet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "TipKamiona") val TipKamiona: String,
    @ColumnInfo(name = "Gorivo") val Gorivo: Int,
    @ColumnInfo(name = "Odrzavanje") val Odrzavanje: Int,
    @ColumnInfo(name = "Putarina") val Putarina: Int,
    @ColumnInfo(name = "Datum") val Datum: String
)
