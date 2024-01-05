package com.cc221002.mcfinalprojectmanuelprammer.ui.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// this is the ViewModel for the addingPage to save the values that are written into the Textfields
class AddingPageViewModel:ViewModel() {
	var location = mutableStateOf("")
	var details = mutableStateOf("")
	var rating = mutableStateOf("")
	var date = mutableStateOf("")


	// this function sets all the variables to empty strings again to "delete" the data in them
	fun deleteAddingPageViewModelEntries(){
		location = mutableStateOf("")
		details = mutableStateOf("")
		rating = mutableStateOf("")
		date = mutableStateOf("")
	}
}