package com.cc221002.mcfinalprojectmanuelprammer.ui.view_model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
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

    private val _selectedTrip = MutableStateFlow<SingleTrip?>(null)
    val selectedTrip: StateFlow<SingleTrip?> = _selectedTrip.asStateFlow()

    private val _openAlertDialogForTrip = mutableStateOf<String?>(null)
    val openAlertDialogForTrip: State<String?> = _openAlertDialogForTrip

//    fun insertPreTrips(){
//        val hardcodedSamples = listOf(
//            SingleTrip("Austria","24.10.2022", "It was very funny", "4"),
//            SingleTrip("Kroatien","28.11.2022", "It was also very funny", "3")
//
//        )
//        viewModelScope.launch{
//            for (trip in hardcodedSamples)
//                dao.insertTrip(trip)
//        }
//    }

    fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

    fun getTrips() {
        viewModelScope.launch {
            dao.getTrips().collect{trips ->
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


    fun editTrip(singleTrip: SingleTrip){
        _mainViewState.update { it.copy(openEditDialog = true, editSingleTrip = singleTrip) }
    }

    fun saveEditedTrip(singleTrip: SingleTrip){
        dismissEditDialog()
        viewModelScope.launch {
            dao.updateTrip(singleTrip)
            getTrips()
        }
    }

    fun dismissEditDialog(){
        _mainViewState.update { it.copy(openEditDialog = false) }
    }


    fun showTripWithID(id:Int) {
        viewModelScope.launch {
            dao.getTripWithID(id).collect { trip ->
                _selectedTrip.value = trip.firstOrNull() // To get the single trip
            }
        }
    }

    fun openAlertDialog(tripId: String) {
        _mainViewState.update { it.copy(openAlertDialog = true) }
        _openAlertDialogForTrip.value = tripId

    }
    fun dismissAlertDialog(){
        _mainViewState.update { it.copy(openAlertDialog = false) }
    }

    fun deleteTrip(singleTrip: SingleTrip, navHostController: NavHostController) {
        viewModelScope.launch() {
            dao.deleteTrip(singleTrip)
            Log.d("Delete","Normal Delete $singleTrip")
//            withContext(Dispatchers.Main) {
                navHostController.navigate(Screen.ShowAllTrips.route)
//            }
        }

    }

//    fun wipeDatabase(){
//        viewModelScope.launch{
//            dao.wipeDatabase()
//        }
//    }

}

