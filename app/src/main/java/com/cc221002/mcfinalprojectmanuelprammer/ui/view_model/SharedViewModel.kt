package com.cc221002.mcfinalprojectmanuelprammer.ui.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// this ViewModel is for saving the image uri so that it can be retrieved from editing and adding
class SharedViewModel {

	private val _imageUri = MutableStateFlow<String?>("")
	val imageUri: StateFlow<String?> = _imageUri

	// this function gets an uri and saves it into the variable above (which gets used to save the uri into the database)
	fun setImageUri(uri: String?) {
		_imageUri.value = uri
	}
}