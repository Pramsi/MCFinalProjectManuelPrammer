package com.cc221002.mcfinalprojectmanuelprammer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cc221002.mcfinalprojectmanuelprammer.data.model.SingleTrip

@Database(entities = [SingleTrip::class], version = 1)

abstract class TripsDatabase : RoomDatabase() {
    abstract val dao: TripsDao
}