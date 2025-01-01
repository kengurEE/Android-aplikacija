package com.example.truckmanagement.Data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "transport")
data class Ent2(
    val TipKamiona : String,
    val Mjesto_Utovara : String,
    val Mjesto_Istovara : String,
    val Km_Utovar : Int,
    val Datum_Utovara : String,
    val Datum_Polaska : String,
    val Mjesto_Polaska : String,
    val Km_Polaska : Int,
    val Usluge_Prevoza_ZaIz : String,
    val Vrsta_Robe : String,
    val Teret_Robe_Kg : Int,
    var Zavrsi : Boolean,
    var Km_Istovar : Int = 0,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)
