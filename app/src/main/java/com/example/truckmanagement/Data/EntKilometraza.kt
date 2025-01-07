package com.example.truckmanagement.Data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "kilometraza")
data class EntKilometraza(
    var Kilometraza : Int,
    var TipKilometraze : String,
    @PrimaryKey(autoGenerate = true) var id_kilometraze: Int = 0

)
