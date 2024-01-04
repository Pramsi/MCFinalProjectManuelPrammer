package com.cc221002.mcfinalprojectmanuelprammer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
//import com.cc221002.mcfinalprojectmanuelprammer.data.TripsDatabase.Companion.MIGRATION_1_2
import com.cc221002.mcfinalprojectmanuelprammer.data.model.SingleTrip

// this creates a room database and defines that the entities of the database is a class called SingleTrip
@Database(entities = [SingleTrip::class], version = 1)
abstract class TripsDatabase : RoomDatabase() {
    // here you define that the dao is the TripsDao.kt
    abstract val dao: TripsDao


    //needed that for migration but you can just delete the app and install it again to solve that problem
//    companion object{
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE TripsList_new (" +
//                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                        "location TEXT NOT NULL, " +
//                        "date TEXT NOT NULL, " +
//                        "details TEXT NOT NULL, " +
//                        "rating TEXT NOT NULL, " +
//                        "imageUri TEXT DEFAULT NULL)")
//
//                // Copy the existing data to the new table
//                database.execSQL("INSERT INTO TripsList_new (id, location, date, details, rating) " +
//                        "SELECT id, location, date, details, rating FROM TripsList")
//
//                // Drop the old table
//                database.execSQL("DROP TABLE TripsList")
//
//                // Rename the new table to the old table's name
//                database.execSQL("ALTER TABLE TripsList_new RENAME TO TripsList")
//            }
//        }
//    }
}