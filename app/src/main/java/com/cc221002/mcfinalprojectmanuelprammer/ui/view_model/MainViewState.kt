package com.cc221002.mcfinalprojectmanuelprammer.ui.view_model

import android.app.AlertDialog
import com.cc221002.mcfinalprojectmanuelprammer.data.model.SingleTrip
import com.cc221002.mcfinalprojectmanuelprammer.ui.view.Screen

// this class defines all the states that are used in the MainViewModel
data class MainViewState(
    // a list where all the trips will be saved
    val trips: List<SingleTrip> = emptyList(),
    // when editing a trip the information from the database gets saved here and is then used for displaying it
    val editSingleTrip: SingleTrip = SingleTrip("","","","",""),
    // this one saves on which screen the user is
    val selectedScreen: Screen = Screen.SplashScreen,

    // those variables are used to manage if Dialogs are open or closed
    val openTripDialog: Boolean = false,
    val openEditDialog: Boolean = false,
    val openAlertDialog: Boolean = false,
    val openAlertDialogForTrip: String = "",
    )