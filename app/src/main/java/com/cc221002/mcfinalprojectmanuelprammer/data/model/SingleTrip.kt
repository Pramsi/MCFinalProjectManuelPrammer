package com.cc221002.mcfinalprojectmanuelprammer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "TripsList")
data class SingleTrip (
    val location: String,
    val date: String,
    val details: String,
    val rating: String,
//    val picture,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)