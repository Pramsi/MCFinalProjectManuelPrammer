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

// this is the MainViewModel where background things are handled for saving and displaying the trips
class MainViewModel (private val dao: TripsDao): ViewModel(){
    // in those variables the states are saved
    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState:StateFlow<MainViewState> = _mainViewState.asStateFlow()

    // this variable is a list of all the trips
    private val _trips = MutableStateFlow<List<SingleTrip>>(emptyList())
    val trips: StateFlow<List<SingleTrip>> = _trips.asStateFlow()

    // this variable is for displaying a single trip
    private val _selectedTrip = MutableStateFlow<SingleTrip?>(null)
    val selectedTrip: StateFlow<SingleTrip?> = _selectedTrip.asStateFlow()

    // this variable is for swipe deleting a trip (it saves for which trip the alert opens)
    private val _openAlertDialogForTrip = mutableStateOf<String?>(null)
    val openAlertDialogForTrip: State<String?> = _openAlertDialogForTrip


    // this function updates on which screen the user currently is
    fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

    // this function calls the dao function to collect all the trips that are saved in the database
    fun getTrips() {
        viewModelScope.launch {
            dao.getTrips().collect{trips ->
                _trips.value = trips
            }
        }
    }

    // this function is called when the user presses the save button. It inserts the data from the TextFields into the database
    fun saveButton(singleTrip: SingleTrip){
        viewModelScope.launch {
            dao.insertTrip(singleTrip)
            // then it calls the getTrips() function to also include the newest trip for displaying
            getTrips()
        }
    }

    // this function is called when the user want to edit a trip.
    fun editTrip(singleTrip: SingleTrip){
        // it opens the editDialog and also gives the data from the trip to be edited to the mainViewState
        _mainViewState.update { it.copy(openEditDialog = true, editSingleTrip = singleTrip) }
    }

    // when the user is finished editing the trip and saves, this function is called
    fun saveEditedTrip(singleTrip: SingleTrip){
        // it closes the editDialog
        dismissEditDialog()
        // and saves the new information into the database
        viewModelScope.launch {
            dao.updateTrip(singleTrip)
            getTrips()
        }
    }

    // this function closes the EditDialog
    fun dismissEditDialog(){
        _mainViewState.update { it.copy(openEditDialog = false) }
    }

    // this function gets the id sent and then gets the Trip that belongs to the id
    fun showTripWithID(id:Int) {
        viewModelScope.launch {
            dao.getTripWithID(id).collect { trip ->
                _selectedTrip.value = trip.firstOrNull() // To get the single trip
            }
        }
    }

    // this function opens the alertDialog
    fun openAlertDialog(tripId: String) {
        _mainViewState.update { it.copy(openAlertDialog = true) }
        // and saves for which the alert is opened
        _openAlertDialogForTrip.value = tripId
    }

    // this function closes the alertDialo
    fun dismissAlertDialog(){
        _mainViewState.update { it.copy(openAlertDialog = false) }
        // and "deletes" for which trip the alert was opened
        _openAlertDialogForTrip.value = ""
    }

    // this function calls the dao function to delete the trip that was passed to it
    fun deleteTrip(singleTrip: SingleTrip, navHostController: NavHostController) {
        viewModelScope.launch() {
            dao.deleteTrip(singleTrip)
            // and then navigates to the ShowAllTrips Screen
            navHostController.navigate(Screen.ShowAllTrips.route)
        }
    }
}


//    these functions are for wiping the whole database and to insert some predefined trips

//    fun wipeDatabase(){
//        viewModelScope.launch{
//            dao.wipeDatabase()
//        }
//    }
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