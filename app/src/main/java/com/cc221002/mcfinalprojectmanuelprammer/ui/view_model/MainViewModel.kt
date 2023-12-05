package com.cc221002.mcfinalprojectmanuelprammer.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc221002.mcfinalprojectmanuelprammer.data.TripsDao
import com.cc221002.mcfinalprojectmanuelprammer.data.model.SingleTrip
import com.cc221002.mcfinalprojectmanuelprammer.ui.view.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel (private val dao: TripsDao): ViewModel(){
    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState:StateFlow<MainViewState> = _mainViewState.asStateFlow()

    private val _trips = MutableStateFlow<List<SingleTrip>>(emptyList())
    val trips: StateFlow<List<SingleTrip>> = _trips.asStateFlow()

    fun insertPreTrips(){
        val hardcodedSamples = listOf(
            SingleTrip("Austria","24.10.2022", "It was very funny", "4"),
            SingleTrip("Kroatien","28.11.2022", "It was also very funny", "3")

        )
        viewModelScope.launch{
            for (trip in hardcodedSamples)
                dao.insertTrip(trip)
        }
    }

    fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

    fun getTrips() {
        viewModelScope.launch {
            dao.getTrips().collect{trips ->
//                _mainViewState.update { it.copy(trips = trips) }
                _trips.value = trips
            }
        }
    }

    fun saveButton(singleTrip: SingleTrip){
        viewModelScope.launch {
            dao.insertTrip(singleTrip)
            getTrips()
        }
    }

    fun showTripWithID(id:Int) {
        viewModelScope.launch {
            _mainViewState.update { it.copy(openTripDialog = true) }
            dao.getTripWithID(id).collect { trip ->
                _trips.value = trip
            }
        }
    }

    fun dismissSingleTripModal(){
        viewModelScope.launch {
            _mainViewState.update { it.copy(openTripDialog = false) }
            getTrips()
        }
    }


    fun deleteTrip(singleTrip: SingleTrip) {
        viewModelScope.launch {
            dao.deleteTrip(singleTrip)
        }
    }

//    fun wipeDatabase(){
//        viewModelScope.launch{
//            dao.wipeDatabase()
//        }
//    }

}

