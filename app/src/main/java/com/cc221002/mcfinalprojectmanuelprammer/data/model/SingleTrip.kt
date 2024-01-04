package com.cc221002.mcfinalprojectmanuelprammer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// defining how my SingleTrip class is built up
// first it is set as an entity of the table called TripsList
@Entity(tableName = "TripsList")
data class SingleTrip (
    val location: String,
    val date: String,
    val details: String,
    val rating: String,
    val imageUri: String?,
    // the Primary Key "id" is automatically generated and starts with 0
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)