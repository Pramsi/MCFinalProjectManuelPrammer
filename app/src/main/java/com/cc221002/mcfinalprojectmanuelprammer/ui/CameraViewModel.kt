package com.cc221002.mcfinalprojectmanuelprammer.ui

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// this is the viewModel for the Camera
class CameraViewModel: ViewModel() {
	// first it gets if the permission is granted and saves it here
	private val _cameraState = MutableStateFlow(CameraState())

	// those variables are for identifying if the navigation to the cameraPreview comes from the adding page or the editing page
	private val _forAdding = mutableStateOf(false)
	val forAdding: State<Boolean> = _forAdding

	private val _forEditing = mutableStateOf(false)
	val forEditing: State<Boolean> = _forEditing

	// this functions changes the CameraPermission based on the value it gets
	// if the value is true the Permission is granted
	fun setCameraPermission(value: Boolean){
		_cameraState.update { it.copy(cameraPermissionGranted = value) }
	}

	// those are the functions to set the variable above, to identify from which screen the navigation comes from, to either true or false
	fun setForAdding(value: Boolean) {
		_forAdding.value = value
	}

	fun setForEditing(value: Boolean) {
		_forEditing.value = value
	}

}