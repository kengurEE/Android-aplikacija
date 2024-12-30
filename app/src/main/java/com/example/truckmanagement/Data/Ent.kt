package com.example.truckmanagement.Data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "entiteti",
    foreignKeys = [
        ForeignKey(
            entity=Ent2::class,
            parentColumns = ["id"],
            childColumns = ["Id_Transporta"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Ent (
    @ColumnInfo(name = "TipKamiona") val TipKamiona: String,
    @ColumnInfo(name = "Gorivo") val Gorivo: Int,
    @ColumnInfo(name = "Odrzavanje") val Odrzavanje: Int,
    @ColumnInfo(name = "Putarina") val Putarina: Int,
    @ColumnInfo(name = "Datum") val Datum: String,
    @ColumnInfo(name = "Id_Transporta") val Id_Transporta: Int,
    @PrimaryKey(autoGenerate = true) var id_troskova: Int = 0

)

