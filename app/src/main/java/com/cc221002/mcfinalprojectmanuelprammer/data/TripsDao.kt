package com.cc221002.mcfinalprojectmanuelprammer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cc221002.mcfinalprojectmanuelprammer.data.model.SingleTrip
import kotlinx.coroutines.flow.Flow


@Dao
interface TripsDao {
    @Insert
    suspend fun insertTrip(singleTrip: SingleTrip)

    @Update
    suspend fun updateTrip(singleTrip: SingleTrip)

    @Delete
    suspend fun deleteTrip(singleTrip: SingleTrip)

//    @Query("DELETE FROM TripsList")
//     fun wipeDatabase()

    @Query("SELECT * FROM TripsList")
    fun getTrips(): Flow<List<SingleTrip>>

    @Query("SELECT * FROM TripsList WHERE id = :id")
    fun getTripWithID(id:Int): Flow<List<SingleTrip>>

}