package com.cc221002.mcfinalprojectmanuelprammer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cc221002.mcfinalprojectmanuelprammer.data.model.SingleTrip
import kotlinx.coroutines.flow.Flow

// This interface is defined as Data Access Object (DAO)
@Dao
interface TripsDao {
    // all the functions below are for working with the database
    // Insert, Update and Delete are Predefined and are ready to use
    // You just create a function which you can call to use the method
    @Insert
    suspend fun insertTrip(singleTrip: SingleTrip)

    @Update
    suspend fun updateTrip(singleTrip: SingleTrip)

    @Delete
    suspend fun deleteTrip(singleTrip: SingleTrip)


    // you are still able to define your own queries
    // this function gets all the information from the table
    @Query("SELECT * FROM TripsList")
    fun getTrips(): Flow<List<SingleTrip>>

    // this function selects the SingleTrip where the id in the table matches the one which is passed to that function
    @Query("SELECT * FROM TripsList WHERE id = :id")
    fun getTripWithID(id:Int): Flow<List<SingleTrip>>


//    This is the Query to wipe the database
//    @Query("DELETE FROM TripsList")
//     fun wipeDatabase()
}