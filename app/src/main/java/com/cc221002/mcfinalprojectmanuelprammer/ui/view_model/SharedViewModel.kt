package com.cc221002.mcfinalprojectmanuelprammer.ui.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel {
	private val _imageUri = MutableStateFlow<String?>("")
	val imageUri: StateFlow<String?> = _imageUri

	fun setImageUri(uri: String?) {
		_imageUri.value = uri
	}
}