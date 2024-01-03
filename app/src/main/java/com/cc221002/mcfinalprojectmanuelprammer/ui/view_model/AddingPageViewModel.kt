package com.cc221002.mcfinalprojectmanuelprammer.ui.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AddingPageViewModel:ViewModel() {
	var location = mutableStateOf("")
	var details = mutableStateOf("")
	var rating = mutableStateOf("")
	var date = mutableStateOf("")


	fun deleteAddingPageViewModelEntries(){
		location = mutableStateOf("")
		details = mutableStateOf("")
		rating = mutableStateOf("")
		date = mutableStateOf("")
	}
}