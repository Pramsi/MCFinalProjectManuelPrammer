package com.cc221002.mcfinalprojectmanuelprammer.ui.view_model

import com.cc221002.mcfinalprojectmanuelprammer.data.model.SingleTrip
import com.cc221002.mcfinalprojectmanuelprammer.ui.view.Screen

data class MainViewState(
    val trips: List<SingleTrip> = emptyList(),
    val editSingleTrip: SingleTrip = SingleTrip("","","",""),
    val selectedScreen: Screen = Screen.First,
    val openTripDialog: Boolean = false,
    val openEditDialog: Boolean = false,

    )